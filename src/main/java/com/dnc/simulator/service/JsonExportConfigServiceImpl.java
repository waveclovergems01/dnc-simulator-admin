package com.dnc.simulator.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dnc.simulator.model.JsonExportConfig;
import com.dnc.simulator.repository.JsonExportConfigRepository;

@Service
public class JsonExportConfigServiceImpl implements JsonExportConfigService {

	private final JsonExportConfigRepository jsonExportConfigRepository;

	public JsonExportConfigServiceImpl(JsonExportConfigRepository jsonExportConfigRepository) {
		this.jsonExportConfigRepository = jsonExportConfigRepository;
	}

	/*
	 * ========================= LIST =========================
	 */
	@Override
	public List<JsonExportConfig> getAllConfigs() {
		return jsonExportConfigRepository.getAllConfigs();
	}

	/*
	 * ========================= GET BY ID =========================
	 */
	@Override
	public JsonExportConfig getConfigById(Integer id) {
		return jsonExportConfigRepository.getConfigById(id);
	}

	/*
	 * ========================= RUN EXPORT SQL ========================= SQL must
	 * return 1 row / 1 column (JSON string)
	 */
	@Override
	@Transactional(readOnly = true)
	public String runExportSql(Integer configId) {

		JsonExportConfig config = jsonExportConfigRepository.getConfigById(configId);

		if (config == null) {
			return null;
		}

		String sql = config.getSqlCommand();

		if (sql == null || sql.trim().isEmpty()) {
			return null;
		}

		// safety guard: SELECT only
		if (!sql.trim().toUpperCase().startsWith("SELECT")) {
			throw new IllegalArgumentException("Export SQL must be SELECT only");
		}

		return jsonExportConfigRepository.executeExportSql(sql);
	}
}
