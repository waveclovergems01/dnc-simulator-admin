package com.dnc.simulator.service;

import java.util.Map;

import com.dnc.simulator.model.SuffixItem;

public interface SuffixCloneService {
	int cloneEquipment(Map<Long, SuffixItem> clones);
}
