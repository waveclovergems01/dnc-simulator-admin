package com.dnc.simulator.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dnc.simulator.model.JsonExportConfig;
import com.dnc.simulator.model.export.ImageExportItem;
import com.dnc.simulator.repository.JsonExportConfigRepository;

@Service
public class JsonExportConfigServiceImpl implements JsonExportConfigService {

	private final JsonExportConfigRepository jsonExportConfigRepository;

	public JsonExportConfigServiceImpl(JsonExportConfigRepository jsonExportConfigRepository) {
		this.jsonExportConfigRepository = jsonExportConfigRepository;
	}

	@Override
	public List<JsonExportConfig> getAllConfigs() {
		return jsonExportConfigRepository.getAllConfigs();
	}

	@Override
	public JsonExportConfig getConfigById(Integer id) {
		return jsonExportConfigRepository.getConfigById(id);
	}

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

		if (!sql.trim().toUpperCase().startsWith("SELECT")) {
			throw new IllegalArgumentException("Export SQL must be SELECT only");
		}

		return jsonExportConfigRepository.executeExportSql(sql);
	}

	@Override
	public String getConfigValue(String code, String name) {
		return jsonExportConfigRepository.getConfigValue(code, name);
	}

	@Override
	public List<ImageExportItem> getEquipmentImages() {
		return jsonExportConfigRepository.getEquipmentImages();
	}
	
	@Override
	public List<ImageExportItem> getPlateImages() {
		return jsonExportConfigRepository.getPlateImages();
	}

	@Override
	public List<ImageExportItem> getCardImages() {
		return jsonExportConfigRepository.getCardImages();
	}
}
