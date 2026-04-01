package com.dnc.simulator.model.export;

public class PlateNameImageExport {

	private Long id;
	private String name;
	private byte[] iconBlob;
	private String iconMime;
	private String iconName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte[] getIconBlob() {
		return iconBlob;
	}

	public void setIconBlob(byte[] iconBlob) {
		this.iconBlob = iconBlob;
	}

	public String getIconMime() {
		return iconMime;
	}

	public void setIconMime(String iconMime) {
		this.iconMime = iconMime;
	}

	public String getIconName() {
		return iconName;
	}

	public void setIconName(String iconName) {
		this.iconName = iconName;
	}
}