package com.dnc.simulator.repository;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.dnc.simulator.model.SuffixItemAbility;

@Repository
public class SuffixItemAbilityRepositoryImpl implements SuffixItemAbilityRepository {

	private final JdbcTemplate jdbcTemplate;

	public SuffixItemAbilityRepositoryImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

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

	@Override
	public Integer insert(SuffixItemAbility ability) {

		String sql = "INSERT INTO m_suffix_item_abilities " + "(suffix_item_id, raw_text, type) " + "VALUES (?, ?, ?)";

		jdbcTemplate.update(sql, ability.getSuffixItemId(), ability.getRawText(), ability.getType());

		// SQLite: last_insert_rowid()
		return jdbcTemplate.queryForObject("SELECT last_insert_rowid()", Integer.class);
	}

	@Override
	public void update(SuffixItemAbility ability) {

		String sql = "UPDATE m_suffix_item_abilities " + "SET raw_text = ?, type = ? " + "WHERE ability_id = ?";

		jdbcTemplate.update(sql, ability.getRawText(), ability.getType(), ability.getAbilityId());
	}

	@Override
	public void delete(Integer abilityId) {
		jdbcTemplate.update("DELETE FROM m_suffix_item_abilities WHERE ability_id = ?", abilityId);
	}
}
