package com.dnc.simulator.model;

import java.util.List;

public class SuffixItemExtraStatForm {

	private Integer suffixItemId;
	private List<SuffixItemExtraStat> stats;

	public Integer getSuffixItemId() {
		return suffixItemId;
	}

	public void setSuffixItemId(Integer suffixItemId) {
		this.suffixItemId = suffixItemId;
	}

	public List<SuffixItemExtraStat> getStats() {
		return stats;
	}

	public void setStats(List<SuffixItemExtraStat> stats) {
		this.stats = stats;
	}
}
