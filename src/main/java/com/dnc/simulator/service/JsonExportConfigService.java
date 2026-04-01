package com.dnc.simulator.service;

import java.util.List;

import com.dnc.simulator.model.JsonExportConfig;
import com.dnc.simulator.model.export.ImageExportItem;

public interface JsonExportConfigService {

	List<JsonExportConfig> getAllConfigs();

	JsonExportConfig getConfigById(Integer id);

	String runExportSql(Integer configId);

	String getConfigValue(String code, String name);

	List<ImageExportItem> getPlateImages();

	List<ImageExportItem> getCardImages();
}