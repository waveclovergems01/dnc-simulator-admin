package com.dnc.simulator.model;

import java.util.ArrayList;
import java.util.List;

public class Job {

	private Integer id;
	private String name;
	private Integer classId;
	private String className;
	private Integer inherit;
	private Integer requiredLevel;

	private List<Integer> nextClassIds = new ArrayList<>();
	
	private Boolean isAdd;

	// getters / setters
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getClassId() {
		return classId;
	}

	public void setClassId(int classId) {
		this.classId = classId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Integer getInherit() {
		return inherit;
	}

	public void setInherit(int inherit) {
		this.inherit = inherit;
	}

	public Integer getRequiredLevel() {
		return requiredLevel;
	}

	public void setRequiredLevel(int requiredLevel) {
		this.requiredLevel = requiredLevel;
	}

	public List<Integer> getNextClassIds() {
		return nextClassIds;
	}

	public void setNextClassIds(List<Integer> nextClassIds) {
		this.nextClassIds = nextClassIds;
	}

	public Boolean getIsAdd() {
		return isAdd;
	}

	public void setIsAdd(Boolean isAdd) {
		this.isAdd = isAdd;
	}
	
}
