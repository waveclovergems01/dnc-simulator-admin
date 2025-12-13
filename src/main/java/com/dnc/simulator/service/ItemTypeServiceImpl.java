package com.dnc.simulator.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dnc.simulator.model.ItemType;
import com.dnc.simulator.repository.ItemTypeRepository;
import com.dnc.simulator.service.ItemTypeService;

@Service
@Transactional
public class ItemTypeServiceImpl implements ItemTypeService {

	private final ItemTypeRepository itemTypeRepository;

	public ItemTypeServiceImpl(ItemTypeRepository itemTypeRepository) {
		this.itemTypeRepository = itemTypeRepository;
	}

	/*
	 * ========================= GET ALL =========================
	 */
	@Override
	public List<ItemType> getAllItemTypes() {
		return itemTypeRepository.findAll();
	}

	/*
	 * ========================= GET BY ID =========================
	 */
	@Override
	public ItemType getItemTypeById(int typeId) {
		return itemTypeRepository.findById(typeId);
	}

	/*
	 * ========================= SAVE (INSERT / UPDATE) =========================
	 */
	@Override
	public void saveItemType(ItemType itemType, boolean isAdd) {

		if (isAdd) {
			itemTypeRepository.insert(itemType);
		} else {
			itemTypeRepository.update(itemType);
		}
	}

	/*
	 * ========================= DELETE =========================
	 */
	@Override
	public void deleteItemType(int typeId) {
		itemTypeRepository.delete(typeId);
	}
}
