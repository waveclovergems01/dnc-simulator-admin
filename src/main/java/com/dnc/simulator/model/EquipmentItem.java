package com.dnc.simulator.model;

import java.util.List;

public class EquipmentItem {

	private Long itemId;
	private String name;
	private Integer typeId;
	private Integer jobId;
	private Integer requiredLevel;
	private Integer rarityId;
	private Integer durability;
	private Integer setId;

	// ใช้ตอน join / edit
	private List<EquipmentItemStat> stats;

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public Integer getJobId() {
		return jobId;
	}

	public void setJobId(Integer jobId) {
		this.jobId = jobId;
	}

	public Integer getRequiredLevel() {
		return requiredLevel;
	}

	public void setRequiredLevel(Integer requiredLevel) {
		this.requiredLevel = requiredLevel;
	}

	public Integer getRarityId() {
		return rarityId;
	}

	public void setRarityId(Integer rarityId) {
		this.rarityId = rarityId;
	}

	public Integer getDurability() {
		return durability;
	}

	public void setDurability(Integer durability) {
		this.durability = durability;
	}

	public Integer getSetId() {
		return setId;
	}

	public void setSetId(Integer setId) {
		this.setId = setId;
	}

	public List<EquipmentItemStat> getStats() {
		return stats;
	}

	public void setStats(List<EquipmentItemStat> stats) {
		this.stats = stats;
	}
}
