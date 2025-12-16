package com.dnc.simulator.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.dnc.simulator.model.JsonExportConfig;

@Repository
public class JsonExportConfigRepositoryImpl implements JsonExportConfigRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/*
	 * ========================= LIST =========================
	 */
	@Override
	public List<JsonExportConfig> getAllConfigs() {

		String sql = "SELECT id, name, export_file_name, sql_command " + "FROM json_export_config ORDER BY id";

		return jdbcTemplate.query(sql, (rs, i) -> {
			JsonExportConfig c = new JsonExportConfig();
			c.setId(rs.getInt("id"));
			c.setName(rs.getString("name"));
			c.setExportFileName(rs.getString("export_file_name"));
			c.setSqlCommand(rs.getString("sql_command"));
			return c;
		});
	}

	/*
	 * ========================= GET BY ID =========================
	 */
	@Override
	public JsonExportConfig getConfigById(Integer id) {

		String sql = "SELECT id, name, export_file_name, sql_command " + "FROM json_export_config WHERE id = ?";

		List<JsonExportConfig> list = jdbcTemplate.query(sql, (rs, i) -> {
			JsonExportConfig c = new JsonExportConfig();
			c.setId(rs.getInt("id"));
			c.setName(rs.getString("name"));
			c.setExportFileName(rs.getString("export_file_name"));
			c.setSqlCommand(rs.getString("sql_command"));
			return c;
		}, id);

		return list.isEmpty() ? null : list.get(0);
	}

	/*
	 * ========================= EXECUTE EXPORT SQL =========================
	 */
	@Override
	public String executeExportSql(String sql) {

		try {
			// SQL ต้อง return 1 row / 1 column (TEXT / JSON)
			return jdbcTemplate.queryForObject(sql, String.class);
		} catch (EmptyResultDataAccessException e) {
			// no row returned → return empty json
			return "[]";
		}
	}
}
