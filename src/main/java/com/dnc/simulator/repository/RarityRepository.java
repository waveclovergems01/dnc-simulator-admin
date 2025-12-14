package com.dnc.simulator.repository;

import java.util.List;
import com.dnc.simulator.model.Rarity;

public interface RarityRepository {

	List<Rarity> findAll();

	Rarity findById(int id);

	void insert(Rarity rarity);

	void update(Rarity rarity);

	void delete(int id);
}
