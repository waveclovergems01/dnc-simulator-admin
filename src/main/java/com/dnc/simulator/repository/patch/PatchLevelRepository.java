package com.dnc.simulator.repository.patch;

import java.util.List;
import java.util.Optional;

import com.dnc.simulator.model.patch.PatchLevel;

public interface PatchLevelRepository {

	void ensureTable();

	List<PatchLevel> findAll();

	Optional<PatchLevel> findById(Long id);

	boolean existsByLevel(Integer level);

	Long insert(Integer level);

	int update(Long id, Integer level);

	int delete(Long id);
}