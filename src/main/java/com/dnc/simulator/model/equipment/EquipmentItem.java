package com.dnc.simulator.model.equipment;

import java.util.ArrayList;
import java.util.List;

import com.dnc.simulator.model.EquipmentItemStat;

public class EquipmentItem {

	private Long itemId;
	private String name;
	private Integer typeId;
	private Integer jobId;
	private Integer requiredLevel;
	private Integer rarityId;
	private Integer durability;
	private Integer setId;

	private byte[] iconBlob;
	private String iconMime;
	private String iconName;

	private List<EquipmentItemStat> stats = new ArrayList<EquipmentItemStat>();

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

	public List<EquipmentItemStat> getStats() {
		return stats;
	}

	public void setStats(List<EquipmentItemStat> stats) {
		this.stats = stats;
	}
}