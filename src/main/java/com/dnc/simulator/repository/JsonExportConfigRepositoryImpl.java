package com.dnc.simulator.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.dnc.simulator.model.JsonExportConfig;
import com.dnc.simulator.model.export.ImageExportItem;

@Repository
public class JsonExportConfigRepositoryImpl implements JsonExportConfigRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

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

	@Override
	public String executeExportSql(String sql) {

		try {
			return jdbcTemplate.queryForObject(sql, String.class);
		} catch (EmptyResultDataAccessException e) {
			return "[]";
		}
	}

	@Override
	public String getConfigValue(String code, String name) {

		String sql = "SELECT value FROM m_config WHERE code = ? AND name = ?";

		try {
			return jdbcTemplate.queryForObject(sql, String.class, code, name);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public List<ImageExportItem> getPlateImages() {

		String sql = "SELECT id, icon_name, icon_mime, icon_blob " + "FROM m_plate_name "
				+ "WHERE icon_blob IS NOT NULL " + "AND LENGTH(icon_blob) > 0";

		return jdbcTemplate.query(sql, (rs, i) -> {
			ImageExportItem item = new ImageExportItem();
			item.setId(rs.getLong("id"));
			item.setComponent("PLATE");
			item.setFileName(rs.getString("icon_name"));
			item.setMimeType(rs.getString("icon_mime"));
			item.setFileBlob(rs.getBytes("icon_blob"));
			return item;
		});
	}

	@Override
	public List<ImageExportItem> getCardImages() {

		String sql = "SELECT id, icon_name, icon_mime, icon_blob " + "FROM m_card_name "
				+ "WHERE icon_blob IS NOT NULL " + "AND LENGTH(icon_blob) > 0";

		return jdbcTemplate.query(sql, (rs, i) -> {
			ImageExportItem item = new ImageExportItem();
			item.setId(rs.getLong("id"));
			item.setComponent("CARD");
			item.setFileName(rs.getString("icon_name"));
			item.setMimeType(rs.getString("icon_mime"));
			item.setFileBlob(rs.getBytes("icon_blob"));
			return item;
		});
	}
	
}
