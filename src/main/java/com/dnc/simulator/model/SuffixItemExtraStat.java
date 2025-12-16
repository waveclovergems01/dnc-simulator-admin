package com.dnc.simulator.model;

public class SuffixItemExtraStat {

	private Integer id;
	private Integer suffixItemId;
	private Integer statId;
	private Double valueMin;
	private Double valueMax;
	private Integer isPercentage; // 0 / 1

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSuffixItemId() {
		return suffixItemId;
	}

	public void setSuffixItemId(Integer suffixItemId) {
		this.suffixItemId = suffixItemId;
	}

	public Integer getStatId() {
		return statId;
	}

	public void setStatId(Integer statId) {
		this.statId = statId;
	}

	public Double getValueMin() {
		return valueMin;
	}

	public void setValueMin(Double valueMin) {
		this.valueMin = valueMin;
	}

	public Double getValueMax() {
		return valueMax;
	}

	public void setValueMax(Double valueMax) {
		this.valueMax = valueMax;
	}

	public Integer getIsPercentage() {
		return isPercentage;
	}

	public void setIsPercentage(Integer isPercentage) {
		this.isPercentage = isPercentage;
	}

	public boolean isPercentage() {
		return isPercentage != null && isPercentage == 1;
	}
}
