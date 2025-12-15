package com.dnc.simulator.service;

import java.util.List;

import com.dnc.simulator.model.SetBonus;

public interface SetBonusService {

	List<SetBonus> getAll();

	SetBonus getById(Integer setId);

	void save(SetBonus setBonus);

	void delete(Integer setId);
}
