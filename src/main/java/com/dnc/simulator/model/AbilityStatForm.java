package com.dnc.simulator.model;

import java.util.List;

public class AbilityStatForm {
	private List<StatForm> stats;

	public static class StatForm {
		private Integer statId;
		private Double valueMin;
		private Double valueMax;
		private Integer isPercentage;

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

	public List<StatForm> getStats() {
		return stats;
	}

	public void setStats(List<StatForm> stats) {
		this.stats = stats;
	}

}
