package com.dnc.simulator.repository;

import java.util.List;
import com.dnc.simulator.model.Stat;

public interface StatRepository {

	List<Stat> findAll();

	Stat findById(int statId);

	void insert(Stat stat);

	void update(Stat stat);

	void delete(int statId);
}
