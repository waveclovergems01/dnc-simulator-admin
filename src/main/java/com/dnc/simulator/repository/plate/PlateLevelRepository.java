package com.dnc.simulator.repository.plate;

import java.util.List;
import java.util.Optional;

import com.dnc.simulator.model.plate.PlateLevel;

public interface PlateLevelRepository {

	void ensureTable();

	List<PlateLevel> findAll();

	Optional<PlateLevel> findById(Long id);

	boolean existsByLevel(Integer level);

	Long insert(Integer level);

	int update(Long id, Integer level);

	int delete(Long id);
}