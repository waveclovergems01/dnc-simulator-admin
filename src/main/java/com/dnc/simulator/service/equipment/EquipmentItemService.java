package com.dnc.simulator.service.equipment;

import java.util.List;

import com.dnc.simulator.model.equipment.EquipmentItem;

public interface EquipmentItemService {

	List<EquipmentItem> getAll();

	EquipmentItem getById(Long itemId);

	EquipmentItem getIconById(Long itemId);

	void save(EquipmentItem item, boolean isAdd);

	void delete(Long itemId);
}