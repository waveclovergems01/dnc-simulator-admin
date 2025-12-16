package com.dnc.simulator.repository;

import java.util.List;
import com.dnc.simulator.model.SuffixItemAbilityStat;

public interface SuffixItemAbilityStatRepository {

	List<SuffixItemAbilityStat> findByAbilityId(Integer abilityId);

	Integer insert(SuffixItemAbilityStat stat);

	void update(SuffixItemAbilityStat stat);

	void delete(Integer id);
}
