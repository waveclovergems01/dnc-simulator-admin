package com.dnc.simulator.repository.plate;

import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.dnc.simulator.model.plate.PlateLevel;

@Repository
public class PlateLevelRepositoryImpl implements PlateLevelRepository {

	private final JdbcTemplate jdbcTemplate;

	public PlateLevelRepositoryImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@PostConstruct
	public void init() {
		ensureTable();
	}

	@Override
	public void ensureTable() {

		String checkSql = "SELECT COUNT(1) FROM sqlite_master WHERE type='table' AND name=?";

		Integer cnt = jdbcTemplate.queryForObject(checkSql, Integer.class, "m_plate_level");
		boolean exists = cnt != null && cnt > 0;

		if (!exists) {

			String createSql = "" + "CREATE TABLE m_plate_level (" + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ " level INTEGER NOT NULL" + ");";

			jdbcTemplate.execute(createSql);
		}
	}

	@Override
	public List<PlateLevel> findAll() {

		String sql = "SELECT id, level FROM m_plate_level ORDER BY level";

		return jdbcTemplate.query(sql, (rs, i) -> {

			PlateLevel t = new PlateLevel();
			t.setId(rs.getLong("id"));
			t.setLevel(rs.getInt("level"));

			return t;
		});
	}

	@Override
	public Optional<PlateLevel> findById(Long id) {

		String sql = "SELECT id, level FROM m_plate_level WHERE id=?";

		List<PlateLevel> list = jdbcTemplate.query(sql, (rs, i) -> {

			PlateLevel t = new PlateLevel();
			t.setId(rs.getLong("id"));
			t.setLevel(rs.getInt("level"));

			return t;

		}, id);

		return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
	}

	@Override
	public boolean existsByLevel(Integer level) {

		String sql = "SELECT COUNT(1) FROM m_plate_level WHERE level=?";

		Integer cnt = jdbcTemplate.queryForObject(sql, Integer.class, level);
		return cnt != null && cnt > 0;
	}

	@Override
	public Long insert(Integer level) {

		String sql = "INSERT INTO m_plate_level(level) VALUES(?)";
		jdbcTemplate.update(sql, level);

		return jdbcTemplate.queryForObject("SELECT last_insert_rowid()", Long.class);
	}

	@Override
	public int update(Long id, Integer level) {

		String sql = "UPDATE m_plate_level SET level=? WHERE id=?";
		return jdbcTemplate.update(sql, level, id);
	}

	@Override
	public int delete(Long id) {

		String sql = "DELETE FROM m_plate_level WHERE id=?";
		return jdbcTemplate.update(sql, id);
	}
}