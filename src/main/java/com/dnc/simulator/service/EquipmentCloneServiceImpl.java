package com.dnc.simulator.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dnc.simulator.model.EquipmentItem;
import com.dnc.simulator.model.EquipmentItemStat;
import com.dnc.simulator.repository.EquipmentCloneRepository;
import com.dnc.simulator.service.EquipmentCloneService;

@Service
public class EquipmentCloneServiceImpl implements EquipmentCloneService {

	private final EquipmentCloneRepository cloneRepo;

	public EquipmentCloneServiceImpl(EquipmentCloneRepository cloneRepo) {
		this.cloneRepo = cloneRepo;
	}

	/*
	 * ========================================================= LOAD SET (พร้อม
	 * stats) =========================================================
	 */
	@Override
	public List<EquipmentItem> getItemsBySetId(Long setId) {

		List<EquipmentItem> items = cloneRepo.findItemsBySetId(setId);

		for (EquipmentItem e : items) {
			List<EquipmentItemStat> stats = cloneRepo.findStatsByItemId(e.getItemId());
			e.setStats(stats);
		}

		return items;
	}

	/*
	 * ========================================================= CLONE SET
	 * (ใช้ค่าที่แก้แล้วจริง)
	 * =========================================================
	 */
	@Override
	@Transactional
	public int cloneSetWithEditedValues(Long setId, Map<Long, EquipmentItem> clones) {

		int success = 0;

		for (Map.Entry<Long, EquipmentItem> entry : clones.entrySet()) {

			EquipmentItem clone = entry.getValue();
			Long newItemId = clone.getItemId();

			// validate
			if (newItemId == null) {
				continue;
			}
			if (cloneRepo.existsItemId(newItemId)) {
				throw new IllegalArgumentException("Item ID already exists: " + newItemId);
			}

			// insert item
			cloneRepo.insertEquipmentItem(clone);

			// insert stats
			if (clone.getStats() != null) {
				for (EquipmentItemStat s : clone.getStats()) {
					s.setItemId(newItemId);
					if (s.getIsPercentage() == null) {
						s.setIsPercentage(0);
					}
					cloneRepo.insertEquipmentItemStat(s);
				}
			}

			success++;
		}

		return success;
	}
}
