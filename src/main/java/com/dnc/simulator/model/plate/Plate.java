package com.dnc.simulator.model.plate;

public class Plate {

	private Long id;
	private Integer typeId;
	private Long plateLevelId;
	private Long plateNameId;
	private Integer rarityId;

	private String plateName;
	private String itemTypeName;
	private Integer level;
	private String rarityName;
	private String color;

	private byte[] iconBlob;
	private String iconMime;
	private String iconName;

	private Integer statId;
	private String statDisplayName;
	private Integer statValue;
	private Double statPercent;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public Long getPlateLevelId() {
		return plateLevelId;
	}

	public void setPlateLevelId(Long plateLevelId) {
		this.plateLevelId = plateLevelId;
	}

	public Long getPlateNameId() {
		return plateNameId;
	}

	public void setPlateNameId(Long plateNameId) {
		this.plateNameId = plateNameId;
	}

	public Integer getRarityId() {
		return rarityId;
	}

	public void setRarityId(Integer rarityId) {
		this.rarityId = rarityId;
	}

	public String getPlateName() {
		return plateName;
	}

	public void setPlateName(String plateName) {
		this.plateName = plateName;
	}

	public String getItemTypeName() {
		return itemTypeName;
	}

	public void setItemTypeName(String itemTypeName) {
		this.itemTypeName = itemTypeName;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getRarityName() {
		return rarityName;
	}

	public void setRarityName(String rarityName) {
		this.rarityName = rarityName;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
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

	public Integer getStatId() {
		return statId;
	}

	public void setStatId(Integer statId) {
		this.statId = statId;
	}

	public String getStatDisplayName() {
		return statDisplayName;
	}

	public void setStatDisplayName(String statDisplayName) {
		this.statDisplayName = statDisplayName;
	}

	public Integer getStatValue() {
		return statValue;
	}

	public void setStatValue(Integer statValue) {
		this.statValue = statValue;
	}

	public Double getStatPercent() {
		return statPercent;
	}

	public void setStatPercent(Double statPercent) {
		this.statPercent = statPercent;
	}
}