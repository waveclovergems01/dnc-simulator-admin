package com.dnc.simulator.service.patch;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dnc.simulator.model.patch.PatchLevel;
import com.dnc.simulator.repository.patch.PatchLevelRepository;

@Service
public class PatchLevelServiceImpl implements PatchLevelService {

	private final PatchLevelRepository patchLevelRepository;

	public PatchLevelServiceImpl(PatchLevelRepository patchLevelRepository) {
		this.patchLevelRepository = patchLevelRepository;
	}

	@Override
	public List<PatchLevel> findAll() {
		return patchLevelRepository.findAll();
	}

	@Override
	public PatchLevel findById(Long id) {
		return patchLevelRepository.findById(id).orElse(null);
	}

	@Override
	public Long create(Integer level) {

		if (patchLevelRepository.existsByLevel(level)) {
			throw new RuntimeException("Level already exists: " + level);
		}

		return patchLevelRepository.insert(level);
	}

	@Override
	public void update(Long id, Integer level) {

		PatchLevel old = findById(id);
		if (old == null) {
			throw new RuntimeException("Level not found: " + id);
		}

		if (!old.getLevel().equals(level) && patchLevelRepository.existsByLevel(level)) {
			throw new RuntimeException("Level already exists: " + level);
		}

		patchLevelRepository.update(id, level);
	}

	@Override
	public void delete(Long id) {
		patchLevelRepository.delete(id);
	}
}