package com.dnc.simulator.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.dnc.simulator.model.SetBonus;
import com.dnc.simulator.model.SetBonusEntry;
import com.dnc.simulator.model.SetBonusStat;

@Repository
public class SetBonusRepositoryImpl implements SetBonusRepository {

	private final JdbcTemplate jdbcTemplate;

	public SetBonusRepositoryImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/* ================= SET ================= */

	@Override
	public List<SetBonus> findAll() {
		return jdbcTemplate.query("SELECT set_id, set_name FROM m_set_bonuses ORDER BY set_id", (rs, i) -> {
			SetBonus s = new SetBonus();
			s.setSetId(rs.getInt("set_id"));
			s.setSetName(rs.getString("set_name"));
			return s;
		});
	}

	@Override
	public SetBonus findById(Integer setId) {
		SetBonus set = jdbcTemplate.queryForObject("SELECT set_id, set_name FROM m_set_bonuses WHERE set_id = ?",
				(rs, i) -> {
					SetBonus s = new SetBonus();
					s.setSetId(rs.getInt("set_id"));
					s.setSetName(rs.getString("set_name"));
					return s;
				}, setId);

		List<SetBonusEntry> entries = jdbcTemplate
				.query("SELECT entry_id, count FROM m_set_bonus_entries WHERE set_id = ? ORDER BY count", (rs, i) -> {
					SetBonusEntry e = new SetBonusEntry();
					e.setEntryId(rs.getInt("entry_id"));
					e.setSetId(setId);
					e.setCount(rs.getInt("count"));
					return e;
				}, setId);

		for (SetBonusEntry e : entries) {
			List<SetBonusStat> stats = jdbcTemplate
					.query("SELECT stat_entry_id, stat_id, value_min, value_max, is_percentage "
							+ "FROM m_set_bonus_stats WHERE entry_id = ?", (rs, i) -> {
								SetBonusStat s = new SetBonusStat();
								s.setStatEntryId(rs.getInt("stat_entry_id"));
								s.setEntryId(e.getEntryId());
								s.setStatId(rs.getInt("stat_id"));
								s.setValueMin(rs.getDouble("value_min"));
								s.setValueMax(rs.getDouble("value_max"));
								s.setIsPercentage(rs.getInt("is_percentage"));
								return s;
							}, e.getEntryId());
			e.setStats(stats);
		}

		set.setEntries(entries);
		return set;
	}

	@Override
	public void insert(SetBonus setBonus) {
		jdbcTemplate.update("INSERT INTO m_set_bonuses (set_id, set_name) VALUES (?, ?)", setBonus.getSetId(),
				setBonus.getSetName());
	}

	@Override
	public Integer insertEntry(SetBonusEntry entry) {

		KeyHolder keyHolder = new GeneratedKeyHolder();

		jdbcTemplate.update(con -> {
			PreparedStatement ps = con.prepareStatement("INSERT INTO m_set_bonus_entries (set_id, count) VALUES (?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, entry.getSetId());
			ps.setInt(2, entry.getCount());
			return ps;
		}, keyHolder);

		return keyHolder.getKey().intValue();
	}

	@Override
	public void insertStat(SetBonusStat stat) {
		jdbcTemplate.update(
				"INSERT INTO m_set_bonus_stats (entry_id, stat_id, value_min, value_max, is_percentage) "
						+ "VALUES (?, ?, ?, ?, ?)",
				stat.getEntryId(), stat.getStatId(), stat.getValueMin(), stat.getValueMax(), stat.getIsPercentage());
	}

	@Override
	public void delete(Integer setId) {
		jdbcTemplate.update("DELETE FROM m_set_bonus_stats WHERE entry_id IN "
				+ "(SELECT entry_id FROM m_set_bonus_entries WHERE set_id = ?)", setId);
		jdbcTemplate.update("DELETE FROM m_set_bonus_entries WHERE set_id = ?", setId);
		jdbcTemplate.update("DELETE FROM m_set_bonuses WHERE set_id = ?", setId);
	}
}
