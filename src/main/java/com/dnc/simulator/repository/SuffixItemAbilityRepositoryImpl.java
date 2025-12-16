package com.dnc.simulator.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.dnc.simulator.model.SuffixItemAbility;

@Repository
public class SuffixItemAbilityRepositoryImpl implements SuffixItemAbilityRepository {

	private final JdbcTemplate jdbcTemplate;

	public SuffixItemAbilityRepositoryImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/*
	 * ===================================================== FIND BY ID
	 * =====================================================
	 */
	@Override
	public SuffixItemAbility findById(Integer abilityId) {

		String sql = "SELECT ability_id, suffix_item_id, raw_text, type " + "FROM m_suffix_item_abilities "
				+ "WHERE ability_id = ?";

		return jdbcTemplate.queryForObject(sql, (rs, i) -> {
			SuffixItemAbility a = new SuffixItemAbility();
			a.setAbilityId(rs.getInt("ability_id"));
			a.setSuffixItemId(rs.getInt("suffix_item_id"));
			a.setRawText(rs.getString("raw_text"));
			a.setType(rs.getString("type"));
			return a;
		}, abilityId);
	}

	/*
	 * ===================================================== FIND BY SUFFIX ITEM ID
	 * =====================================================
	 */
	@Override
	public List<SuffixItemAbility> findBySuffixItemId(Integer suffixItemId) {

		String sql = "SELECT ability_id, suffix_item_id, raw_text, type " + "FROM m_suffix_item_abilities "
				+ "WHERE suffix_item_id = ? " + "ORDER BY ability_id";

		return jdbcTemplate.query(sql, (rs, i) -> {
			SuffixItemAbility a = new SuffixItemAbility();
			a.setAbilityId(rs.getInt("ability_id"));
			a.setSuffixItemId(rs.getInt("suffix_item_id"));
			a.setRawText(rs.getString("raw_text"));
			a.setType(rs.getString("type"));
			return a;
		}, suffixItemId);
	}

	/*
	 * ===================================================== INSERT
	 * =====================================================
	 */
	@Override
	public Integer insert(SuffixItemAbility ability) {

		String sql = "INSERT INTO m_suffix_item_abilities " + "(suffix_item_id, raw_text, type) " + "VALUES (?, ?, ?)";

		KeyHolder keyHolder = new GeneratedKeyHolder();

		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, ability.getSuffixItemId());
			ps.setString(2, ability.getRawText());
			ps.setString(3, ability.getType());
			return ps;
		}, keyHolder);

		return keyHolder.getKey().intValue();
	}

	/*
	 * ===================================================== UPDATE
	 * =====================================================
	 */
	@Override
	public void update(SuffixItemAbility ability) {

		String sql = "UPDATE m_suffix_item_abilities " + "SET raw_text = ?, type = ? " + "WHERE ability_id = ?";

		jdbcTemplate.update(sql, ability.getRawText(), ability.getType(), ability.getAbilityId());
	}

	/*
	 * ===================================================== DELETE
	 * =====================================================
	 */
	@Override
	public void delete(Integer abilityId) {

		String sql = "DELETE FROM m_suffix_item_abilities WHERE ability_id = ?";

		jdbcTemplate.update(sql, abilityId);
	}
	
	@Override
	public void deleteBySuffixItemId(Integer suffixItemId) {

		String sql = "DELETE FROM m_suffix_item_abilities WHERE suffix_item_id = ?";

		jdbcTemplate.update(sql, suffixItemId);
	}
}
