package com.dnc.simulator.repository;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.dnc.simulator.model.SuffixItemAbilityStat;

@Repository
public class SuffixItemAbilityStatRepositoryImpl implements SuffixItemAbilityStatRepository {

	private final JdbcTemplate jdbcTemplate;

	public SuffixItemAbilityStatRepositoryImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

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

	@Override
	public Integer insert(SuffixItemAbilityStat stat) {

		String sql = "INSERT INTO m_suffix_item_ability_stats "
				+ "(ability_id, stat_id, value_min, value_max, is_percentage) " + "VALUES (?, ?, ?, ?, ?)";

		jdbcTemplate.update(sql, stat.getAbilityId(), stat.getStatId(), stat.getValueMin(), stat.getValueMax(),
				stat.getIsPercentage());

		return jdbcTemplate.queryForObject("SELECT last_insert_rowid()", Integer.class);
	}

	@Override
	public void update(SuffixItemAbilityStat stat) {

		String sql = "UPDATE m_suffix_item_ability_stats "
				+ "SET stat_id = ?, value_min = ?, value_max = ?, is_percentage = ? " + "WHERE id = ?";

		jdbcTemplate.update(sql, stat.getStatId(), stat.getValueMin(), stat.getValueMax(), stat.getIsPercentage(),
				stat.getId());
	}

	@Override
	public void delete(Integer id) {
		jdbcTemplate.update("DELETE FROM m_suffix_item_ability_stats WHERE id = ?", id);
	}
}
