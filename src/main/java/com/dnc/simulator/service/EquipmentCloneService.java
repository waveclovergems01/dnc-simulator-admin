package com.dnc.simulator.service;

import java.util.List;
import java.util.Map;

import com.dnc.simulator.model.EquipmentItem;

public interface EquipmentCloneService {

	List<EquipmentItem> getItemsBySetId(Long setId);

	int cloneSetWithEditedValues(Long setId, Map<Long, EquipmentItem> clones);
}
