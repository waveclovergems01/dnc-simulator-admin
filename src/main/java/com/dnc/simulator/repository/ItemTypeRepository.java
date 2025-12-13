package com.dnc.simulator.repository;

import java.util.List;

import com.dnc.simulator.model.ItemType;

public interface ItemTypeRepository {

	List<ItemType> findAll();

	ItemType findById(int typeId);

	void insert(ItemType itemType);

	void update(ItemType itemType);

	void delete(int typeId);
}
