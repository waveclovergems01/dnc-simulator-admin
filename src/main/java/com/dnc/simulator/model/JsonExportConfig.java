package com.dnc.simulator.model;

public class JsonExportConfig {

	private Integer id;
	private String name;
	private String exportFileName;
	private String sqlCommand;

	// getter / setter
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExportFileName() {
		return exportFileName;
	}

	public void setExportFileName(String exportFileName) {
		this.exportFileName = exportFileName;
	}

	public String getSqlCommand() {
		return sqlCommand;
	}

	public void setSqlCommand(String sqlCommand) {
		this.sqlCommand = sqlCommand;
	}
}
