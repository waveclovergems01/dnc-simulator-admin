package com.dnc.simulator.service.plate;

import java.util.List;

import com.dnc.simulator.model.plate.Plate;

public interface PlateService {

	List<Plate> findAll();

	Plate findById(Long id);

	Plate findIconById(Long id);

	boolean existsDuplicate(Long plateTypeId, Long plateLevelId, Long plateNameId, Integer rarityId, Long excludeId);

	Long create(Long plateTypeId, Long plateLevelId, Long plateNameId, Integer rarityId, Integer statId,
			Integer statValue, Double statPercent);

	void update(Long id, Long plateTypeId, Long plateLevelId, Long plateNameId, Integer rarityId, Integer statId,
			Integer statValue, Double statPercent);

	void delete(Long id);
}