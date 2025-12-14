package com.dnc.simulator.model;

public class SuffixGroupItem {

	private Integer id;
	private Integer groupId;
	private Integer suffixId;
	private String mode; // normal / pvp

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public Integer getSuffixId() {
		return suffixId;
	}

	public void setSuffixId(Integer suffixId) {
		this.suffixId = suffixId;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}
}
