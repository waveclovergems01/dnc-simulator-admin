package com.dnc.simulator.service.plate;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dnc.simulator.model.plate.Plate;
import com.dnc.simulator.repository.plate.PlateRepository;

@Service
public class PlateServiceImpl implements PlateService {

	private final PlateRepository plateRepository;

	public PlateServiceImpl(PlateRepository plateRepository) {
		this.plateRepository = plateRepository;
	}

	@Override
	public List<Plate> findAll() {
		return plateRepository.findAll();
	}

	@Override
	public Plate findById(Long id) {
		return plateRepository.findById(id).orElse(null);
	}

	@Override
	public Plate findIconById(Long id) {
		return plateRepository.findIconById(id).orElse(null);
	}

	@Override
	public boolean existsDuplicate(Integer typeId, Long plateLevelId, Long plateNameId, Integer rarityId,
			Long excludeId) {
		return plateRepository.existsDuplicate(typeId, plateLevelId, plateNameId, rarityId, excludeId);
	}

	@Override
	public Long create(Integer typeId, Long plateLevelId, Long plateNameId, Integer rarityId, Integer statId,
			Integer statValue, Double statPercent) {

		validate(typeId, plateLevelId, plateNameId, rarityId, statId, statValue, statPercent);

		if (plateRepository.existsDuplicate(typeId, plateLevelId, plateNameId, rarityId, null)) {
			throw new RuntimeException("Duplicate plate: same Item Type + Patch Level + Plate Name + Rarity already exists");
		}

		return plateRepository.insert(typeId, plateLevelId, plateNameId, rarityId, statId, statValue, statPercent);
	}

	@Override
	public void update(Long id, Integer typeId, Long plateLevelId, Long plateNameId, Integer rarityId, Integer statId,
			Integer statValue, Double statPercent) {

		if (id == null) {
			throw new RuntimeException("id is required");
		}

		validate(typeId, plateLevelId, plateNameId, rarityId, statId, statValue, statPercent);

		if (plateRepository.existsDuplicate(typeId, plateLevelId, plateNameId, rarityId, id)) {
			throw new RuntimeException("Duplicate plate: same Item Type + Patch Level + Plate Name + Rarity already exists");
		}

		plateRepository.update(id, typeId, plateLevelId, plateNameId, rarityId, statId, statValue, statPercent);
	}

	@Override
	public void delete(Long id) {
		plateRepository.delete(id);
	}

	private void validate(Integer typeId, Long plateLevelId, Long plateNameId, Integer rarityId, Integer statId,
			Integer statValue, Double statPercent) {

		if (typeId == null) {
			throw new RuntimeException("typeId is required");
		}

		if (plateLevelId == null) {
			throw new RuntimeException("plateLevelId is required");
		}

		if (plateNameId == null) {
			throw new RuntimeException("plateNameId is required");
		}

		if (rarityId == null) {
			throw new RuntimeException("rarityId is required");
		}

		// ถ้ายังใช้ rule เดิม
		if (Integer.valueOf(1).equals(typeId)) {
			if (statId == null) {
				throw new RuntimeException("statId is required when typeId is 1");
			}
		}

		if (statValue != null && statValue < 0) {
			throw new RuntimeException("statValue must be >= 0");
		}

		if (statPercent != null && statPercent < 0) {
			throw new RuntimeException("statPercent must be >= 0");
		}
	}
}