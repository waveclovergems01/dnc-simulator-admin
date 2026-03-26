package com.dnc.simulator.service.plate;

import java.util.List;

import com.dnc.simulator.model.plate.PlateType;

public interface PlateTypeService {

	List<PlateType> findAll();

	PlateType findById(Long id);

	boolean existsByName(String name);

	Long create(String name);

	void update(Long id, String name);

	void delete(Long id);
}