package com.dnc.simulator.service;

import java.util.List;

import com.dnc.simulator.model.JsonExportConfig;

public interface JsonExportConfigService {

	List<JsonExportConfig> getAllConfigs();

	JsonExportConfig getConfigById(Integer id);

	String runExportSql(Integer configId);
}
