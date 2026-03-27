package com.dnc.simulator.repository.plate;

import java.util.List;
import java.util.Optional;

import com.dnc.simulator.model.plate.PlateThirdStat;

public interface PlateThirdStatRepository {

	void ensureTable();

	List<PlateThirdStat> findAll();

	Optional<PlateThirdStat> findById(Long id);

	Long insert(Integer statId, Integer rarityId, Long patchLevelId, Integer value);

	int update(Long id, Integer statId, Integer rarityId, Long patchLevelId, Integer value);

	int delete(Long id);
}