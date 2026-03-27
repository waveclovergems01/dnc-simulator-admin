package com.dnc.simulator.service.plate;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dnc.simulator.model.plate.PlateThirdStat;
import com.dnc.simulator.repository.plate.PlateThirdStatRepository;

@Service
public class PlateThirdStatServiceImpl implements PlateThirdStatService {

	private final PlateThirdStatRepository plateThirdStatRepository;

	public PlateThirdStatServiceImpl(PlateThirdStatRepository plateThirdStatRepository) {
		this.plateThirdStatRepository = plateThirdStatRepository;
	}

	@Override
	public List<PlateThirdStat> findAll() {
		return plateThirdStatRepository.findAll();
	}

	@Override
	public PlateThirdStat findById(Long id) {
		return plateThirdStatRepository.findById(id).orElse(null);
	}

	@Override
	public Long create(Integer statId, Integer rarityId, Long patchLevelId, Integer value) {
		validate(statId, rarityId, patchLevelId, value);
		return plateThirdStatRepository.insert(statId, rarityId, patchLevelId, value);
	}

	@Override
	public void update(Long id, Integer statId, Integer rarityId, Long patchLevelId, Integer value) {
		if (id == null) {
			throw new RuntimeException("id is required");
		}

		validate(statId, rarityId, patchLevelId, value);
		plateThirdStatRepository.update(id, statId, rarityId, patchLevelId, value);
	}

	@Override
	public void delete(Long id) {
		plateThirdStatRepository.delete(id);
	}

	private void validate(Integer statId, Integer rarityId, Long patchLevelId, Integer value) {
		if (statId == null) {
			throw new RuntimeException("statId is required");
		}

		if (rarityId == null) {
			throw new RuntimeException("rarityId is required");
		}

		if (patchLevelId == null) {
			throw new RuntimeException("patchLevelId is required");
		}

		if (value == null) {
			throw new RuntimeException("value is required");
		}

		if (value < 0) {
			throw new RuntimeException("value must be >= 0");
		}
	}
}