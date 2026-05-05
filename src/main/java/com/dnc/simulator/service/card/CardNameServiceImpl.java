package com.dnc.simulator.service.card;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dnc.simulator.model.card.CardName;
import com.dnc.simulator.repository.card.CardNameRepository;

@Service
public class CardNameServiceImpl implements CardNameService {

	private final CardNameRepository cardNameRepository;

	public CardNameServiceImpl(CardNameRepository cardNameRepository) {
		this.cardNameRepository = cardNameRepository;
	}

	@Override
	public List<CardName> findAll() {
		return cardNameRepository.findAll();
	}

	@Override
	public CardName findById(Long id) {
		return cardNameRepository.findById(id).orElse(null);
	}

	@Override
	public CardName findIconById(Long id) {
		return cardNameRepository.findIconById(id).orElse(null);
	}

	@Override
	public boolean existsByName(String name, Long excludeId) {
		return cardNameRepository.existsByName(name, excludeId);
	}

	@Override
	public Long create(String name, byte[] iconBlob, String iconMime, String iconName) {
		validateName(name);

		if (cardNameRepository.existsByName(name.trim(), null)) {
			throw new RuntimeException("Duplicate Card Name");
		}

		return cardNameRepository.insert(name.trim(), iconBlob, iconMime, iconName);
	}

	@Override
	public void update(Long id, String name, byte[] iconBlob, String iconMime, String iconName) {
		if (id == null) {
			throw new RuntimeException("id is required");
		}

		validateName(name);

		if (cardNameRepository.existsByName(name.trim(), id)) {
			throw new RuntimeException("Duplicate Card Name");
		}

		cardNameRepository.update(id, name.trim(), iconBlob, iconMime, iconName);
	}

	@Override
	public void updateNoIcon(Long id, String name) {
		if (id == null) {
			throw new RuntimeException("id is required");
		}

		validateName(name);

		if (cardNameRepository.existsByName(name.trim(), id)) {
			throw new RuntimeException("Duplicate Card Name");
		}

		cardNameRepository.updateNoIcon(id, name.trim());
	}

	@Override
	public void delete(Long id) {
		if (id == null) {
			throw new RuntimeException("id is required");
		}

		cardNameRepository.delete(id);
	}

	private void validateName(String name) {
		if (name == null || name.trim().isEmpty()) {
			throw new RuntimeException("Card Name is required");
		}

		if (!name.trim().matches("^[A-Za-z0-9'\\-]+( [A-Za-z0-9'\\-]+)*$")) {
			throw new RuntimeException(
					"Card Name must contain only English letters, numbers, spaces, hyphen, and apostrophe");
		}
	}
}