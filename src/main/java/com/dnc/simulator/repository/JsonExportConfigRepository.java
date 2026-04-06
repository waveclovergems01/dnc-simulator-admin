package com.dnc.simulator.repository;

import java.util.List;

import com.dnc.simulator.model.JsonExportConfig;
import com.dnc.simulator.model.export.ImageExportItem;

public interface JsonExportConfigRepository {

	List<JsonExportConfig> getAllConfigs();

	JsonExportConfig getConfigById(Integer id);

	String executeExportSql(String sql);

	String getConfigValue(String code, String name);
	
	List<ImageExportItem> getEquipmentImages();

	List<ImageExportItem> getPlateImages();

	List<ImageExportItem> getCardImages();

}