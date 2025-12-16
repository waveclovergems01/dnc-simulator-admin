package com.dnc.simulator.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.dnc.simulator.model.SuffixItemAbilityStat;

@Repository
public class SuffixItemAbilityStatRepositoryImpl implements SuffixItemAbilityStatRepository {

	private final JdbcTemplate jdbcTemplate;

	public SuffixItemAbilityStatRepositoryImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/*
	 * ===================================================== FIND BY ABILITY ID
	 * =====================================================
	 */
	@Override
	public List<SuffixItemAbilityStat> findByAbilityId(Integer abilityId) {

		String sql = "SELECT id, ability_id, stat_id, value_min, value_max, is_percentage "
				+ "FROM m_suffix_item_ability_stats " + "WHERE ability_id = ? " + "ORDER BY id";

		return jdbcTemplate.query(sql, (rs, i) -> {
			SuffixItemAbilityStat s = new SuffixItemAbilityStat();
			s.setId(rs.getInt("id"));
			s.setAbilityId(rs.getInt("ability_id"));
			s.setStatId(rs.getInt("stat_id"));
			s.setValueMin(rs.getDouble("value_min"));
			s.setValueMax(rs.getDouble("value_max"));
			s.setIsPercentage(rs.getInt("is_percentage"));
			return s;
		}, abilityId);
	}

	/*
	 * ===================================================== INSERT
	 * =====================================================
	 */
	@Override
	public Integer insert(SuffixItemAbilityStat stat) {

		String sql = "INSERT INTO m_suffix_item_ability_stats "
				+ "(ability_id, stat_id, value_min, value_max, is_percentage) " + "VALUES (?, ?, ?, ?, ?)";

		KeyHolder keyHolder = new GeneratedKeyHolder();

		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, stat.getAbilityId());
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
	public void update(SuffixItemAbilityStat stat) {

		String sql = "UPDATE m_suffix_item_ability_stats "
				+ "SET stat_id = ?, value_min = ?, value_max = ?, is_percentage = ? " + "WHERE id = ?";

		jdbcTemplate.update(sql, stat.getStatId(), stat.getValueMin(), stat.getValueMax(), stat.getIsPercentage(),
				stat.getId());
	}

	/*
	 * ===================================================== DELETE (single)
	 * =====================================================
	 */
	@Override
	public void delete(Integer id) {

		String sql = "DELETE FROM m_suffix_item_ability_stats WHERE id = ?";

		jdbcTemplate.update(sql, id);
	}

	/*
	 * ===================================================== DELETE BY ABILITY ID
	 * (สำคัญสำหรับ save) =====================================================
	 */
	@Override
	public void deleteByAbilityId(Integer abilityId) {

		String sql = "DELETE FROM m_suffix_item_ability_stats WHERE ability_id = ?";

		jdbcTemplate.update(sql, abilityId);
	}
}
