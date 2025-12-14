package com.dnc.simulator.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.dnc.simulator.model.Stat;
import com.dnc.simulator.repository.StatRepository;

@Repository
public class StatRepositoryImpl implements StatRepository {

	private final JdbcTemplate jdbcTemplate;

	public StatRepositoryImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private static class StatRowMapper implements RowMapper<Stat> {
		@Override
		public Stat mapRow(ResultSet rs, int rowNum) throws SQLException {
			Stat s = new Stat();
			s.setStatId(rs.getInt("stat_id"));
			s.setStatName(rs.getString("stat_name"));
			s.setDisplayName(rs.getString("display_name"));
			s.setStatCatId(rs.getInt("stat_cat_id"));
			s.setStatCatName(rs.getString("stat_cat_name"));
			s.setIsPercentage(rs.getInt("is_percentage") == 1);
			return s;
		}
	}

	@Override
	public List<Stat> findAll() {
		return jdbcTemplate.query("SELECT * FROM m_stats ORDER BY stat_id", new StatRowMapper());
	}

	@Override
	public Stat findById(int statId) {
		List<Stat> list = jdbcTemplate.query("SELECT * FROM m_stats WHERE stat_id = ?", new StatRowMapper(), statId);
		return list.isEmpty() ? null : list.get(0);
	}

	@Override
	public void insert(Stat stat) {
		jdbcTemplate.update(
				"INSERT INTO m_stats (stat_id, stat_name, display_name, stat_cat_id, stat_cat_name, is_percentage) "
						+ "VALUES (?, ?, ?, ?, ?, ?)",
				stat.getStatId(), stat.getStatName(), stat.getDisplayName(), stat.getStatCatId(), stat.getStatCatName(),
				stat.isPercentage() ? 1 : 0);
	}

	@Override
	public void update(Stat stat) {
		jdbcTemplate.update(
				"UPDATE m_stats SET stat_name=?, display_name=?, stat_cat_id=?, stat_cat_name=?, is_percentage=? "
						+ "WHERE stat_id=?",
				stat.getStatName(), stat.getDisplayName(), stat.getStatCatId(), stat.getStatCatName(),
				stat.isPercentage() ? 1 : 0, stat.getStatId());
	}

	@Override
	public void delete(int statId) {
		jdbcTemplate.update("DELETE FROM m_stats WHERE stat_id = ?", statId);
	}
}
