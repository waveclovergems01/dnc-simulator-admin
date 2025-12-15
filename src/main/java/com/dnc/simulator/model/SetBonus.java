package com.dnc.simulator.model;

import java.util.List;

public class SetBonus {

	private Integer setId;
	private String setName;
	private List<SetBonusEntry> entries;

	public Integer getSetId() {
		return setId;
	}

	public void setSetId(Integer setId) {
		this.setId = setId;
	}

	public String getSetName() {
		return setName;
	}

	public void setSetName(String setName) {
		this.setName = setName;
	}

	public List<SetBonusEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<SetBonusEntry> entries) {
		this.entries = entries;
	}
}
