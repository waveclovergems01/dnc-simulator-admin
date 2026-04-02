package com.dnc.simulator.service.equipment;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dnc.simulator.model.EquipmentItemStat;
import com.dnc.simulator.model.equipment.EquipmentItem;
import com.dnc.simulator.repository.equipment.EquipmentItemRepository;

@Service
public class EquipmentItemServiceImpl implements EquipmentItemService {

	private final EquipmentItemRepository repo;

	public EquipmentItemServiceImpl(EquipmentItemRepository repo) {
		this.repo = repo;
	}

	@Override
	public List<EquipmentItem> getAll() {
		return repo.findAll();
	}

	@Override
	public EquipmentItem getById(Long itemId) {
		return repo.findById(itemId);
	}

	@Override
	public EquipmentItem getIconById(Long itemId) {
		return repo.findIconById(itemId);
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
				s.setItemId(item.getItemId());

				if (s.getIsPercentage() == null) {
					s.setIsPercentage(0);
				}

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