package com.dnc.simulator.service.plate;

import java.util.List;

import com.dnc.simulator.model.plate.PlateThirdStat;

public interface PlateThirdStatService {

	List<PlateThirdStat> findAll();

	PlateThirdStat findById(Long id);

	Long create(Integer statId, Integer rarityId, Long patchLevelId, Integer value);

	void update(Long id, Integer statId, Integer rarityId, Long patchLevelId, Integer value);

	void delete(Long id);
}