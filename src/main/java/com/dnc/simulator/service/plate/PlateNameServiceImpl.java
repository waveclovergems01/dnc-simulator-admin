package com.dnc.simulator.service.plate;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dnc.simulator.model.plate.PlateName;
import com.dnc.simulator.repository.plate.PlateNameRepository;

@Service
public class PlateNameServiceImpl implements PlateNameService {

	private final PlateNameRepository plateNameRepository;

	public PlateNameServiceImpl(PlateNameRepository plateNameRepository) {
		this.plateNameRepository = plateNameRepository;
	}

	@Override
	public List<PlateName> findAll() {
		return plateNameRepository.findAll();
	}

	@Override
	public PlateName findById(Long id) {
		return plateNameRepository.findById(id).orElse(null);
	}

	@Override
	public PlateName findIconById(Long id) {
		return plateNameRepository.findIconById(id).orElse(null);
	}

	@Override
	public Long create(String name, byte[] iconBlob, String iconMime, String iconName) {
		name = normalize(name);

		if (name.isEmpty()) {
			throw new RuntimeException("Plate Name is required");
		}

		return plateNameRepository.insert(name, iconBlob, iconMime, iconName);
	}

	@Override
	public void update(Long id, String name, byte[] iconBlob, String iconMime, String iconName) {
		if (id == null) {
			throw new RuntimeException("id is required");
		}

		name = normalize(name);

		if (name.isEmpty()) {
			throw new RuntimeException("Plate Name is required");
		}

		plateNameRepository.update(id, name, iconBlob, iconMime, iconName);
	}

	@Override
	public void updateNoIcon(Long id, String name) {
		if (id == null) {
			throw new RuntimeException("id is required");
		}

		name = normalize(name);

		if (name.isEmpty()) {
			throw new RuntimeException("Plate Name is required");
		}

		plateNameRepository.updateNoIcon(id, name);
	}

	@Override
	public void delete(Long id) {
		plateNameRepository.delete(id);
	}

	private String normalize(String name) {
		name = name == null ? "" : name.trim();
		name = name.replaceAll("\\s+", " ");
		return name;
	}
}