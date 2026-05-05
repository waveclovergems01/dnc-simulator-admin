package com.dnc.simulator.repository.card;

import java.util.List;
import java.util.Optional;

import com.dnc.simulator.model.card.CardName;

public interface CardNameRepository {

	void ensureTable();

	List<CardName> findAll();

	Optional<CardName> findById(Long id);

	Optional<CardName> findIconById(Long id);

	boolean existsByName(String name, Long excludeId);

	Long insert(String name, byte[] iconBlob, String iconMime, String iconName);

	int update(Long id, String name, byte[] iconBlob, String iconMime, String iconName);

	int updateNoIcon(Long id, String name);

	int delete(Long id);
}