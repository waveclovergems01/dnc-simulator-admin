package com.dnc.simulator.model;

import java.util.List;

public class SuffixItemAbility {

	private Integer abilityId;
	private Integer suffixItemId;
	private String rawText;
	private String type;

	// ใช้ตอน join ข้อมูล
	private List<SuffixItemAbilityStat> abilityStats;

	public Integer getAbilityId() {
		return abilityId;
	}

	public void setAbilityId(Integer abilityId) {
		this.abilityId = abilityId;
	}

	public Integer getSuffixItemId() {
		return suffixItemId;
	}

	public void setSuffixItemId(Integer suffixItemId) {
		this.suffixItemId = suffixItemId;
	}

	public String getRawText() {
		return rawText;
	}

	public void setRawText(String rawText) {
		this.rawText = rawText;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<SuffixItemAbilityStat> getAbilityStats() {
		return abilityStats;
	}

	public void setAbilityStats(List<SuffixItemAbilityStat> abilityStats) {
		this.abilityStats = abilityStats;
	}
}
