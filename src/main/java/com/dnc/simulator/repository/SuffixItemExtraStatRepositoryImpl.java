package com.dnc.simulator.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.dnc.simulator.model.SuffixItemExtraStat;

@Repository
public class SuffixItemExtraStatRepositoryImpl implements SuffixItemExtraStatRepository {

	private final JdbcTemplate jdbcTemplate;

	public SuffixItemExtraStatRepositoryImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/*
	 * ===================================================== FIND BY SUFFIX ITEM ID
	 * =====================================================
	 */
	@Override
	public List<SuffixItemExtraStat> findBySuffixItemId(Integer suffixItemId) {

		String sql = "SELECT id, suffix_item_id, stat_id, value_min, value_max, is_percentage "
				+ "FROM m_suffix_item_extra_stats " + "WHERE suffix_item_id = ? " + "ORDER BY id";

		return jdbcTemplate.query(sql, (rs, i) -> {
			SuffixItemExtraStat s = new SuffixItemExtraStat();
			s.setId(rs.getInt("id"));
			s.setSuffixItemId(rs.getInt("suffix_item_id"));
			s.setStatId(rs.getInt("stat_id"));
			s.setValueMin(rs.getDouble("value_min"));
			s.setValueMax(rs.getDouble("value_max"));
			s.setIsPercentage(rs.getInt("is_percentage"));
			return s;
		}, suffixItemId);
	}

	/*
	 * ===================================================== INSERT
	 * =====================================================
	 */
	@Override
	public Integer insert(SuffixItemExtraStat stat) {

		String sql = "INSERT INTO m_suffix_item_extra_stats "
				+ "(suffix_item_id, stat_id, value_min, value_max, is_percentage) " + "VALUES (?, ?, ?, ?, ?)";

		KeyHolder keyHolder = new GeneratedKeyHolder();

		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, stat.getSuffixItemId());
			ps.setInt(2, stat.getStatId());
			ps.setDouble(3, stat.getValueMin());
			ps.setDouble(4, stat.getValueMax());
			ps.setInt(5, stat.getIsPercentage());
			return ps;
		}, keyHolder);

		return keyHolder.getKey().intValue();
	}

	/*
	 * ===================================================== UPDATE
	 * =====================================================
	 */
	@Override
	public void update(SuffixItemExtraStat stat) {

		String sql = "UPDATE m_suffix_item_extra_stats "
				+ "SET stat_id = ?, value_min = ?, value_max = ?, is_percentage = ? " + "WHERE id = ?";

		jdbcTemplate.update(sql, stat.getStatId(), stat.getValueMin(), stat.getValueMax(), stat.getIsPercentage(),
				stat.getId());
	}

	/*
	 * ===================================================== DELETE (ของเดิม –
	 * ลบทีละ row) =====================================================
	 */
	@Override
	public void delete(Integer id) {

		String sql = "DELETE FROM m_suffix_item_extra_stats WHERE id = ?";

		jdbcTemplate.update(sql, id);
	}

	/*
	 * ===================================================== DELETE BY SUFFIX ITEM
	 * ID (เพิ่มใหม่) =====================================================
	 */
	@Override
	public void deleteBySuffixItemId(Integer suffixItemId) {

		String sql = "DELETE FROM m_suffix_item_extra_stats WHERE suffix_item_id = ?";

		jdbcTemplate.update(sql, suffixItemId);
	}
}
