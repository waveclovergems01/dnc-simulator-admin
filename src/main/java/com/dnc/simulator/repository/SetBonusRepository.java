package com.dnc.simulator.repository;

import java.util.List;

import com.dnc.simulator.model.SetBonus;
import com.dnc.simulator.model.SetBonusEntry;
import com.dnc.simulator.model.SetBonusStat;

public interface SetBonusRepository {

	List<SetBonus> findAll();

	SetBonus findById(Integer setId);

	void insert(SetBonus setBonus);

	Integer insertEntry(SetBonusEntry entry);

	void insertStat(SetBonusStat stat);

	void delete(Integer setId);
}
