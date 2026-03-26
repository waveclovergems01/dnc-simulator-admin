package com.dnc.simulator.repository.plate;

import java.util.List;
import java.util.Optional;

import com.dnc.simulator.model.plate.Plate;

public interface PlateRepository {

	void ensureTable();

	List<Plate> findAll();

	Optional<Plate> findById(Long id);

	Optional<Plate> findIconById(Long id);

	Long insert(Long plateTypeId, Long plateLevelId, Integer rarityId, String plateName, byte[] iconBlob,
			String iconMime, String iconName);

	int update(Long id, Long plateTypeId, Long plateLevelId, Integer rarityId, String plateName, byte[] iconBlob,
			String iconMime, String iconName);

	int updateNoIcon(Long id, Long plateTypeId, Long plateLevelId, Integer rarityId, String plateName);

	int delete(Long id);
}