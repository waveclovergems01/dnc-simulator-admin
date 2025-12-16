package com.dnc.simulator.repository;

import java.util.List;

import com.dnc.simulator.model.JsonExportConfig;

public interface JsonExportConfigRepository {

	/*
	 * ========================= LIST =========================
	 */
	List<JsonExportConfig> getAllConfigs();

	/*
	 * ========================= GET BY ID =========================
	 */
	JsonExportConfig getConfigById(Integer id);

	/*
	 * ========================= EXECUTE EXPORT SQL ========================= SQL
	 * must return 1 row / 1 column (JSON string)
	 */
	String executeExportSql(String sql);
}
