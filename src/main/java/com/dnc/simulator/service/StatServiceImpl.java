package com.dnc.simulator.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dnc.simulator.model.Stat;
import com.dnc.simulator.repository.StatRepository;
import com.dnc.simulator.service.StatService;

@Service
@Transactional
public class StatServiceImpl implements StatService {

	private final StatRepository statRepository;

	public StatServiceImpl(StatRepository statRepository) {
		this.statRepository = statRepository;
	}

	@Override
	public List<Stat> getAllStats() {
		return statRepository.findAll();
	}

	@Override
	public Stat getStatById(int statId) {
		return statRepository.findById(statId);
	}

	@Override
	public void saveStat(Stat stat, boolean isAdd) {
		if (isAdd) {
			statRepository.insert(stat);
		} else {
			statRepository.update(stat);
		}
	}

	@Override
	public void deleteStat(int statId) {
		statRepository.delete(statId);
	}
}
