package com.dnc.simulator.service.plate;

import java.util.List;

import com.dnc.simulator.model.plate.Plate;

public interface PlateService {

	List<Plate> findAll();

	Plate findById(Long id);

	Plate findIconById(Long id);

	Long create(Long plateTypeId, Long plateLevelId, Integer rarityId, String plateName, byte[] iconBlob,
			String iconMime, String iconName);

	void update(Long id, Long plateTypeId, Long plateLevelId, Integer rarityId, String plateName, byte[] iconBlob,
			String iconMime, String iconName);

	void updateNoIcon(Long id, Long plateTypeId, Long plateLevelId, Integer rarityId, String plateName);

	void delete(Long id);
}