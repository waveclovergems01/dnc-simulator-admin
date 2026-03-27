package com.dnc.simulator.model.plate;

public class PlateThirdStat {

	private Long id;
	private Integer statId;
	private Integer rarityId;
	private Long patchLevelId;
	private Integer value;

	private String statDisplayName;
	private String rarityName;
	private String color;
	private Integer level;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getStatId() {
		return statId;
	}

	public void setStatId(Integer statId) {
		this.statId = statId;
	}

	public Integer getRarityId() {
		return rarityId;
	}

	public void setRarityId(Integer rarityId) {
		this.rarityId = rarityId;
	}

	public Long getPatchLevelId() {
		return patchLevelId;
	}

	public void setPatchLevelId(Long patchLevelId) {
		this.patchLevelId = patchLevelId;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public String getStatDisplayName() {
		return statDisplayName;
	}

	public void setStatDisplayName(String statDisplayName) {
		this.statDisplayName = statDisplayName;
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

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
}