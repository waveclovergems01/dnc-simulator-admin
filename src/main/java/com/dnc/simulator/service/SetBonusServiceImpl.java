package com.dnc.simulator.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dnc.simulator.model.SetBonus;
import com.dnc.simulator.model.SetBonusEntry;
import com.dnc.simulator.model.SetBonusStat;
import com.dnc.simulator.repository.SetBonusRepository;

@Service
public class SetBonusServiceImpl implements SetBonusService {

	private final SetBonusRepository repository;

	public SetBonusServiceImpl(SetBonusRepository repository) {
		this.repository = repository;
	}

	@Override
	public List<SetBonus> getAll() {
		return repository.findAll();
	}

	@Override
	public SetBonus getById(Integer setId) {
		return repository.findById(setId);
	}

	/**
	 * SAVE แบบ delete แล้ว insert ใหม่ทั้งชุด
	 */
	@Override
	@Transactional
	public void save(SetBonus setBonus) {

		if (setBonus == null || setBonus.getSetId() == null) {
			throw new IllegalArgumentException("setBonus or setId is null");
		}

		// 1️⃣ clear old data
		repository.delete(setBonus.getSetId());

		// 2️⃣ insert set
		repository.insert(setBonus);

		if (setBonus.getEntries() == null) {
			return;
		}

		// 3️⃣ insert entries + stats
		for (SetBonusEntry entry : setBonus.getEntries()) {

			if (entry.getCount() == null) {
				continue;
			}

			entry.setSetId(setBonus.getSetId());

			// ⭐ get real entry_id
			Integer entryId = repository.insertEntry(entry);

			if (entryId == null || entry.getStats() == null) {
				continue;
			}

			for (SetBonusStat stat : entry.getStats()) {

				// skip empty stat
				if (stat.getStatId() == null) {
					continue;
				}

				stat.setEntryId(entryId);

				// normalize integer stat
				if (stat.getIsPercentage() == null || stat.getIsPercentage() == 0) {
					if (stat.getValueMin() != null) {
						stat.setValueMin(Math.floor(stat.getValueMin()));
					}
					if (stat.getValueMax() != null) {
						stat.setValueMax(Math.floor(stat.getValueMax()));
					}
				}

				repository.insertStat(stat);
			}
		}
	}

	@Override
	@Transactional
	public void delete(Integer setId) {
		repository.delete(setId);
	}
}
