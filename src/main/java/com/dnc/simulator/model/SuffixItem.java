package com.dnc.simulator.model;

import java.util.List;

public class SuffixItem {

	private Long id;
	private Long itemId;
	private Integer suffixTypeId;
	private String name;
	private Integer tier;

	private List<SuffixItemAbility> abilities;
	private List<SuffixItemExtraStat> extraStats;

	// getters & setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public Integer getSuffixTypeId() {
		return suffixTypeId;
	}

	public void setSuffixTypeId(Integer suffixTypeId) {
		this.suffixTypeId = suffixTypeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<SuffixItemAbility> getAbilities() {
		return abilities;
	}

	public void setAbilities(List<SuffixItemAbility> abilities) {
		this.abilities = abilities;
	}

	public List<SuffixItemExtraStat> getExtraStats() {
		return extraStats;
	}

	public void setExtraStats(List<SuffixItemExtraStat> extraStats) {
		this.extraStats = extraStats;
	}

	public Integer getTier() {
		return tier;
	}

	public void setTier(Integer tier) {
		this.tier = tier;
	}
	
}
