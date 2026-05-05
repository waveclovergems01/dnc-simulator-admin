package com.dnc.simulator.service.card;

import java.util.List;

import com.dnc.simulator.model.card.CardName;

public interface CardNameService {

	List<CardName> findAll();

	CardName findById(Long id);

	CardName findIconById(Long id);

	boolean existsByName(String name, Long excludeId);

	Long create(String name, byte[] iconBlob, String iconMime, String iconName);

	void update(Long id, String name, byte[] iconBlob, String iconMime, String iconName);

	void updateNoIcon(Long id, String name);

	void delete(Long id);
}