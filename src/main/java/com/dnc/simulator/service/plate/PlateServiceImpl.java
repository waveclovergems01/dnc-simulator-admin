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
	public boolean existsDuplicate(Long plateTypeId, Long plateLevelId, Long plateNameId, Integer rarityId,
			Long excludeId) {
		return plateRepository.existsDuplicate(plateTypeId, plateLevelId, plateNameId, rarityId, excludeId);
	}

	@Override
	public Long create(Long plateTypeId, Long plateLevelId, Long plateNameId, Integer rarityId, Integer statId,
			Integer statValue, Double statPercent) {

		validate(plateTypeId, plateLevelId, plateNameId, rarityId, statId, statValue, statPercent);

		if (plateRepository.existsDuplicate(plateTypeId, plateLevelId, plateNameId, rarityId, null)) {
			throw new RuntimeException("Duplicate plate: same Type + Patch Level + Plate Name + Rarity already exists");
		}

		return plateRepository.insert(plateTypeId, plateLevelId, plateNameId, rarityId, statId, statValue, statPercent);
	}

	@Override
	public void update(Long id, Long plateTypeId, Long plateLevelId, Long plateNameId, Integer rarityId, Integer statId,
			Integer statValue, Double statPercent) {

		if (id == null) {
			throw new RuntimeException("id is required");
		}

		validate(plateTypeId, plateLevelId, plateNameId, rarityId, statId, statValue, statPercent);

		if (plateRepository.existsDuplicate(plateTypeId, plateLevelId, plateNameId, rarityId, id)) {
			throw new RuntimeException("Duplicate plate: same Type + Patch Level + Plate Name + Rarity already exists");
		}

		plateRepository.update(id, plateTypeId, plateLevelId, plateNameId, rarityId, statId, statValue, statPercent);
	}

	@Override
	public void delete(Long id) {
		plateRepository.delete(id);
	}

	private void validate(Long plateTypeId, Long plateLevelId, Long plateNameId, Integer rarityId, Integer statId,
			Integer statValue, Double statPercent) {

		if (plateTypeId == null) {
			throw new RuntimeException("plateTypeId is required");
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

		if (Long.valueOf(1L).equals(plateTypeId)) {
			if (statId == null) {
				throw new RuntimeException("statId is required when plateTypeId is 1");
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