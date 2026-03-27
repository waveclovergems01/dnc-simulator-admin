package com.dnc.simulator.repository.plate;

import java.util.List;
import java.util.Optional;

import com.dnc.simulator.model.plate.Plate;

public interface PlateRepository {

	void ensureTable();

	List<Plate> findAll();

	Optional<Plate> findById(Long id);

	Optional<Plate> findIconById(Long id);

	boolean existsDuplicate(Long plateTypeId, Long plateLevelId, Long plateNameId, Integer rarityId, Long excludeId);

	Long insert(Long plateTypeId, Long plateLevelId, Long plateNameId, Integer rarityId, Integer statId,
			Integer statValue, Double statPercent);

	int update(Long id, Long plateTypeId, Long plateLevelId, Long plateNameId, Integer rarityId, Integer statId,
			Integer statValue, Double statPercent);

	int delete(Long id);
}