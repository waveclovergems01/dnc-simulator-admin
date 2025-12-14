package com.dnc.simulator.model;

public class EquipmentItemStat {

	private Long itemId;
	private Integer statId;
	private Integer valueMin;
	private Integer valueMax;
	private Integer isPercentage; // 0 / 1

	
	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
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
}
