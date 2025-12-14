package com.dnc.simulator.service;

import java.util.List;
import com.dnc.simulator.model.*;

public interface EquipmentItemService {

	List<EquipmentItem> getAll();

	EquipmentItem getById(Long itemId);

	void save(EquipmentItem item, boolean isAdd);

	void delete(Long itemId);
}
