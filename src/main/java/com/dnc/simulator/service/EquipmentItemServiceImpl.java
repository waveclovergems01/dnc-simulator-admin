package com.dnc.simulator.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dnc.simulator.model.EquipmentItem;
import com.dnc.simulator.model.EquipmentItemStat;
import com.dnc.simulator.repository.EquipmentItemRepository;
import com.dnc.simulator.service.EquipmentItemService;

@Service
public class EquipmentItemServiceImpl implements EquipmentItemService {

	private final EquipmentItemRepository repo;

	public EquipmentItemServiceImpl(EquipmentItemRepository repo) {
		this.repo = repo;
	}

	@Override
	public java.util.List<EquipmentItem> getAll() {
		return repo.findAll();
	}

	@Override
	public EquipmentItem getById(Long itemId) {
		return repo.findById(itemId);
	}

	@Override
	@Transactional
	public void save(EquipmentItem item, boolean isAdd) {

		if (isAdd) {
			repo.insert(item);
		} else {
			repo.update(item);
			repo.deleteStatsByItemId(item.getItemId());
		}

		if (item.getStats() != null) {
			for (EquipmentItemStat s : item.getStats()) {
				repo.insertStat(s);
			}
		}
	}

	@Override
	public void delete(Long itemId) {
		repo.deleteStatsByItemId(itemId);
		repo.delete(itemId);
	}
}
