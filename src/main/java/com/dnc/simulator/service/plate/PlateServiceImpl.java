package com.dnc.simulator.service.plate;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dnc.simulator.model.plate.Plate;
import com.dnc.simulator.repository.plate.PlateRepository;

@Service
public class PlateServiceImpl implements PlateService {

	private final PlateRepository plateRepository;

	public PlateServiceImpl(PlateRepository plateRepository) {
		this.plateRepository = plateRepository;
	}

	@Override
	public List<Plate> findAll() {
		return plateRepository.findAll();
	}

	@Override
	public Plate findById(Long id) {
		return plateRepository.findById(id).orElse(null);
	}

	@Override
	public Plate findIconById(Long id) {
		return plateRepository.findIconById(id).orElse(null);
	}

	@Override
	public Long create(Long plateTypeId, Long plateLevelId, Integer rarityId, String plateName, byte[] iconBlob,
			String iconMime, String iconName) {

		validate(plateTypeId, plateLevelId, rarityId, plateName);

		// icon สามารถ null ได้
		if (iconBlob != null && iconBlob.length > 0) {
			if (iconMime == null || iconMime.trim().isEmpty()) {
				throw new RuntimeException("iconMime is required when iconBlob is present");
			}
		}

		return plateRepository.insert(plateTypeId, plateLevelId, rarityId, plateName.trim(), iconBlob,
				safeTrim(iconMime), safeTrim(iconName));
	}

	@Override
	public void update(Long id, Long plateTypeId, Long plateLevelId, Integer rarityId, String plateName,
			byte[] iconBlob, String iconMime, String iconName) {

		if (id == null || id <= 0)
			throw new RuntimeException("id is required");
		validate(plateTypeId, plateLevelId, rarityId, plateName);

		if (iconBlob != null && iconBlob.length > 0) {
			if (iconMime == null || iconMime.trim().isEmpty()) {
				throw new RuntimeException("iconMime is required when iconBlob is present");
			}
		}

		plateRepository.update(id, plateTypeId, plateLevelId, rarityId, plateName.trim(), iconBlob, safeTrim(iconMime),
				safeTrim(iconName));
	}

	@Override
	public void updateNoIcon(Long id, Long plateTypeId, Long plateLevelId, Integer rarityId, String plateName) {
		if (id == null || id <= 0)
			throw new RuntimeException("id is required");
		validate(plateTypeId, plateLevelId, rarityId, plateName);
		plateRepository.updateNoIcon(id, plateTypeId, plateLevelId, rarityId, plateName.trim());
	}

	@Override
	public void delete(Long id) {
		plateRepository.delete(id);
	}

	private void validate(Long plateTypeId, Long plateLevelId, Integer rarityId, String plateName) {

		if (plateTypeId == null || plateTypeId <= 0)
			throw new RuntimeException("plateTypeId is required");
		if (plateLevelId == null || plateLevelId <= 0)
			throw new RuntimeException("plateLevelId is required");
		if (rarityId == null || rarityId <= 0)
			throw new RuntimeException("rarityId is required");

		plateName = plateName == null ? "" : plateName.trim();
		if (plateName.isEmpty())
			throw new RuntimeException("plateName is required");
	}

	private String safeTrim(String s) {
		return s == null ? null : s.trim();
	}
}