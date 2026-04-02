package com.dnc.simulator.repository.equipment;

import java.util.List;

import com.dnc.simulator.model.EquipmentItemStat;
import com.dnc.simulator.model.equipment.EquipmentItem;

public interface EquipmentCloneRepository {

	void ensureTable();

	List<EquipmentItem> findItemsBySetId(Long setId);

	List<EquipmentItemStat> findStatsByItemId(Long itemId);

	void insertEquipmentItem(EquipmentItem item);

	void insertEquipmentItemStat(EquipmentItemStat stat);

	boolean existsItemId(Long itemId);
}