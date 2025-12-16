package com.dnc.simulator.model;

public class SuffixItemAbilityStat {

	private Integer id;
	private Integer abilityId;
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

	public Integer getAbilityId() {
		return abilityId;
	}

	public void setAbilityId(Integer abilityId) {
		this.abilityId = abilityId;
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

	// helper
	public boolean isPercentage() {
		return isPercentage != null && isPercentage == 1;
	}
}
