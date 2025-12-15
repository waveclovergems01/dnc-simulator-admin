package com.dnc.simulator.repository;

import java.util.List;

import com.dnc.simulator.model.EquipmentItem;
import com.dnc.simulator.model.EquipmentItemStat;

public interface EquipmentCloneRepository {

	List<EquipmentItem> findItemsBySetId(Long setId);

	List<EquipmentItemStat> findStatsByItemId(Long itemId);

	void insertEquipmentItem(EquipmentItem item);

	void insertEquipmentItemStat(EquipmentItemStat stat);

	boolean existsItemId(Long itemId);
}
