package com.dnc.simulator.repository;

import java.util.List;
import com.dnc.simulator.model.SuffixItemAbility;

public interface SuffixItemAbilityRepository {

	List<SuffixItemAbility> findBySuffixItemId(Integer suffixItemId);

	SuffixItemAbility findById(Integer abilityId);

	Integer insert(SuffixItemAbility ability);

	void update(SuffixItemAbility ability);

	void delete(Integer abilityId);
}
