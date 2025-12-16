package com.dnc.simulator.model;

public class SuffixStat {

	private Long id;
	private Long suffixItemId;
	private Integer statId;
	private Integer valueMin;
	private Integer valueMax;
	private Integer isPercentage;
	private String statType; // ABILITY | EXTRA

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSuffixItemId() {
		return suffixItemId;
	}

	public void setSuffixItemId(Long suffixItemId) {
		this.suffixItemId = suffixItemId;
	}

	public Integer getStatId() {
		return statId;
	}

	public void setStatId(Integer statId) {
		this.statId = statId;
	}

	public Integer getValueMin() {
		return valueMin;
	}

	public void setValueMin(Integer valueMin) {
		this.valueMin = valueMin;
	}

	public Integer getValueMax() {
		return valueMax;
	}

	public void setValueMax(Integer valueMax) {
		this.valueMax = valueMax;
	}

	public Integer getIsPercentage() {
		return isPercentage;
	}

	public void setIsPercentage(Integer isPercentage) {
		this.isPercentage = isPercentage;
	}

	public String getStatType() {
		return statType;
	}

	public void setStatType(String statType) {
		this.statType = statType;
	}

}
