package com.dnc.simulator.repository.plate;

import java.util.List;
import java.util.Optional;

import com.dnc.simulator.model.plate.PlateType;

public interface PlateTypeRepository {

	void ensureTable();

	List<PlateType> findAll();

	Optional<PlateType> findById(Long id);

	boolean existsByName(String name);

	Long insert(String name);

	int update(Long id, String name);

	int delete(Long id);
}
