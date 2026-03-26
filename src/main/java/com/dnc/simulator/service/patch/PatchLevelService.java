package com.dnc.simulator.service.patch;

import java.util.List;

import com.dnc.simulator.model.patch.PatchLevel;

public interface PatchLevelService {

	List<PatchLevel> findAll();

	PatchLevel findById(Long id);

	Long create(Integer level);

	void update(Long id, Integer level);

	void delete(Long id);
}