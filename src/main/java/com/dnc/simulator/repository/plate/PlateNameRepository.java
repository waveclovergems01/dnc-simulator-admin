package com.dnc.simulator.repository.plate;

import java.util.List;
import java.util.Optional;

import com.dnc.simulator.model.plate.PlateName;

public interface PlateNameRepository {

	List<PlateName> findAll();

	Optional<PlateName> findById(Long id);

	Optional<PlateName> findIconById(Long id);

	Long insert(String name, byte[] iconBlob, String iconMime, String iconName);

	int update(Long id, String name, byte[] iconBlob, String iconMime, String iconName);

	int updateNoIcon(Long id, String name);

	int delete(Long id);
}