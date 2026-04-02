package com.dnc.simulator.repository.equipment;

import java.util.List;

import com.dnc.simulator.model.EquipmentItemStat;
import com.dnc.simulator.model.equipment.EquipmentItem;

public interface EquipmentItemRepository {

	void ensureTable();

	List<EquipmentItem> findAll();

	EquipmentItem findById(Long itemId);

	EquipmentItem findIconById(Long itemId);

	void insert(EquipmentItem item);

	void update(EquipmentItem item);

	void delete(Long itemId);

	List<EquipmentItemStat> findStatsByItemId(Long itemId);

	void insertStat(EquipmentItemStat stat);

	void deleteStatsByItemId(Long itemId);
}