package com.dnc.simulator.model;

public class SetBonusStat {

	private Integer statEntryId;
	private Integer entryId;
	private Integer statId;
	private Double valueMin;
	private Double valueMax;
	private Integer isPercentage;

	public Integer getStatEntryId() {
		return statEntryId;
	}

	public void setStatEntryId(Integer statEntryId) {
		this.statEntryId = statEntryId;
	}

	public Integer getEntryId() {
		return entryId;
	}

	public void setEntryId(Integer entryId) {
		this.entryId = entryId;
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
}
