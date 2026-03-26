package com.dnc.simulator.service.plate;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dnc.simulator.model.plate.PlateLevel;
import com.dnc.simulator.repository.plate.PlateLevelRepository;

@Service
public class PlateLevelServiceImpl implements PlateLevelService {

	private final PlateLevelRepository plateLevelRepository;

	public PlateLevelServiceImpl(PlateLevelRepository plateLevelRepository) {
		this.plateLevelRepository = plateLevelRepository;
	}

	@Override
	public List<PlateLevel> findAll() {
		return plateLevelRepository.findAll();
	}

	@Override
	public PlateLevel findById(Long id) {
		return plateLevelRepository.findById(id).orElse(null);
	}

	@Override
	public Long create(Integer level) {

		// กัน level ซ้ำ (ถ้าอยากให้ซ้ำได้ เอาบล็อคนี้ออกได้)
		if (plateLevelRepository.existsByLevel(level)) {
			throw new RuntimeException("Level already exists: " + level);
		}

		return plateLevelRepository.insert(level);
	}

	@Override
	public void update(Long id, Integer level) {

		PlateLevel old = findById(id);
		if (old == null) {
			throw new RuntimeException("Level not found: " + id);
		}

		if (!old.getLevel().equals(level) && plateLevelRepository.existsByLevel(level)) {
			throw new RuntimeException("Level already exists: " + level);
		}

		plateLevelRepository.update(id, level);
	}

	@Override
	public void delete(Long id) {
		plateLevelRepository.delete(id);
	}
}