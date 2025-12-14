package com.dnc.simulator.repository;

import java.util.List;
import com.dnc.simulator.model.*;

public interface EquipmentItemRepository {

	List<EquipmentItem> findAll();

	EquipmentItem findById(Long itemId);

	void insert(EquipmentItem item);

	void update(EquipmentItem item);

	void delete(Long itemId);

	// stats
	List<EquipmentItemStat> findStatsByItemId(Long itemId);

	void insertStat(EquipmentItemStat stat);

	void deleteStatsByItemId(Long itemId);
}
