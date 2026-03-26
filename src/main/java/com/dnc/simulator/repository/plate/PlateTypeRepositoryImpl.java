package com.dnc.simulator.repository.plate;

import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.dnc.simulator.model.plate.PlateType;

@Repository
public class PlateTypeRepositoryImpl implements PlateTypeRepository {

	private final JdbcTemplate jdbcTemplate;

	public PlateTypeRepositoryImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@PostConstruct
	public void init() {
		ensureTable();
	}

	@Override
	public void ensureTable() {

		// SQLite: check table exists
		String checkSql = "SELECT COUNT(1) FROM sqlite_master WHERE type='table' AND name=?";

		Integer cnt = jdbcTemplate.queryForObject(checkSql, Integer.class, "m_plate_type");
		boolean exists = cnt != null && cnt > 0;

		if (!exists) {

			String createSql = "" + "CREATE TABLE m_plate_type (" + "  id   INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "  name TEXT NOT NULL" + ");";

			jdbcTemplate.execute(createSql);

			// (optional) index กันชื่อซ้ำ ถ้าต้องการให้ชื่อไม่ซ้ำ ให้ปลดคอมเมนต์
			// jdbcTemplate.execute("CREATE UNIQUE INDEX ux_m_plate_type_name ON
			// m_plate_type(name);");
		}
	}

	@Override
	public List<PlateType> findAll() {

		String sql = "SELECT id, name FROM m_plate_type ORDER BY id";

		return jdbcTemplate.query(sql, (rs, i) -> {
			PlateType t = new PlateType();
			t.setId(rs.getLong("id"));
			t.setName(rs.getString("name"));
			return t;
		});
	}

	@Override
	public Optional<PlateType> findById(Long id) {

		String sql = "SELECT id, name FROM m_plate_type WHERE id = ?";

		List<PlateType> list = jdbcTemplate.query(sql, (rs, i) -> {
			PlateType t = new PlateType();
			t.setId(rs.getLong("id"));
			t.setName(rs.getString("name"));
			return t;
		}, id);

		return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
	}

	@Override
	public boolean existsByName(String name) {

		String sql = "SELECT COUNT(1) FROM m_plate_type WHERE lower(name) = lower(?)";

		Integer cnt = jdbcTemplate.queryForObject(sql, Integer.class, name);
		return cnt != null && cnt > 0;
	}

	@Override
	public Long insert(String name) {

		String sql = "INSERT INTO m_plate_type (name) VALUES (?)";
		jdbcTemplate.update(sql, name);

		// SQLite: last inserted id (same connection)
		Long newId = jdbcTemplate.queryForObject("SELECT last_insert_rowid()", Long.class);
		return newId;
	}

	@Override
	public int update(Long id, String name) {

		String sql = "UPDATE m_plate_type SET name = ? WHERE id = ?";
		return jdbcTemplate.update(sql, name, id);
	}

	@Override
	public int delete(Long id) {

		String sql = "DELETE FROM m_plate_type WHERE id = ?";
		return jdbcTemplate.update(sql, id);
	}
}