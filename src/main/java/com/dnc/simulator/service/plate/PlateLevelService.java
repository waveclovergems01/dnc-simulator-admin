package com.dnc.simulator.service.plate;

import java.util.List;

import com.dnc.simulator.model.plate.PlateLevel;

public interface PlateLevelService {

	List<PlateLevel> findAll();

	PlateLevel findById(Long id);

	Long create(Integer level);

	void update(Long id, Integer level);

	void delete(Long id);
}