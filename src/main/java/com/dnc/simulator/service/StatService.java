package com.dnc.simulator.service;

import java.util.List;
import com.dnc.simulator.model.Stat;

public interface StatService {

	List<Stat> getAllStats();

	Stat getStatById(int statId);

	void saveStat(Stat stat, boolean isAdd);

	void deleteStat(int statId);
}
