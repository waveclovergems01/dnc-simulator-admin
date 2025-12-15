package com.dnc.simulator.model;

import java.util.List;

public class SetBonusEntry {

	private Integer entryId;
	private Integer setId;
	private Integer count;
	private List<SetBonusStat> stats;

	public Integer getEntryId() {
		return entryId;
	}

	public void setEntryId(Integer entryId) {
		this.entryId = entryId;
	}

	public Integer getSetId() {
		return setId;
	}

	public void setSetId(Integer setId) {
		this.setId = setId;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public List<SetBonusStat> getStats() {
		return stats;
	}

	public void setStats(List<SetBonusStat> stats) {
		this.stats = stats;
	}
}
