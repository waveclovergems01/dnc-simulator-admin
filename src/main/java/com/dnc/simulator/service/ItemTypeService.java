package com.dnc.simulator.service;

import java.util.List;

import com.dnc.simulator.model.ItemType;

public interface ItemTypeService {

	List<ItemType> getAllItemTypes();

	ItemType getItemTypeById(int typeId);

	void saveItemType(ItemType itemType, boolean isAdd);

	void deleteItemType(int typeId);
}
