package com.dnc.simulator.service.plate;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dnc.simulator.model.plate.PlateType;
import com.dnc.simulator.repository.plate.PlateTypeRepository;

@Service
public class PlateTypeServiceImpl implements PlateTypeService {

	private final PlateTypeRepository plateTypeRepository;

	public PlateTypeServiceImpl(PlateTypeRepository plateTypeRepository) {
		this.plateTypeRepository = plateTypeRepository;
	}

	@Override
	public List<PlateType> findAll() {
		return plateTypeRepository.findAll();
	}

	@Override
	public PlateType findById(Long id) {
		return plateTypeRepository.findById(id).orElse(null);
	}

	@Override
	public boolean existsByName(String name) {
		return plateTypeRepository.existsByName(name);
	}

	@Override
	public Long create(String name) {

		if (plateTypeRepository.existsByName(name)) {
			throw new RuntimeException("Plate type already exists");
		}

		return plateTypeRepository.insert(name);
	}

	@Override
	public void update(Long id, String name) {
		plateTypeRepository.update(id, name);
	}

	@Override
	public void delete(Long id) {
		plateTypeRepository.delete(id);
	}
}