package com.dnc.simulator.service.plate;

import java.util.List;

import com.dnc.simulator.model.plate.PlateName;

public interface PlateNameService {

	List<PlateName> findAll();

	PlateName findById(Long id);

	PlateName findIconById(Long id);

	Long create(String name, byte[] iconBlob, String iconMime, String iconName);

	void update(Long id, String name, byte[] iconBlob, String iconMime, String iconName);

	void updateNoIcon(Long id, String name);

	void delete(Long id);
}