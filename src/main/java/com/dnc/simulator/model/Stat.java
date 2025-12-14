package com.dnc.simulator.model;

public class Stat {

	private Integer statId;
	private String statName;
	private String displayName;
	private Integer statCatId;
	private String statCatName;
	private boolean isPercentage;

	public Integer getStatId() {
		return statId;
	}

	public void setStatId(Integer statId) {
		this.statId = statId;
	}

	public String getStatName() {
		return statName;
	}

	public void setStatName(String statName) {
		this.statName = statName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Integer getStatCatId() {
		return statCatId;
	}

	public void setStatCatId(Integer statCatId) {
		this.statCatId = statCatId;
	}

	public String getStatCatName() {
		return statCatName;
	}

	public void setStatCatName(String statCatName) {
		this.statCatName = statCatName;
	}

	public boolean isPercentage() {
		return isPercentage;
	}

	public void setIsPercentage(boolean isPercentage) {
		this.isPercentage = isPercentage;
	}
}
