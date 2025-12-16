package com.dnc.simulator.repository;

import java.util.List;
import com.dnc.simulator.model.SuffixItemAbility;

public interface SuffixItemAbilityRepository {

	/* ===================== Query ===================== */

	SuffixItemAbility findById(Integer abilityId);

	List<SuffixItemAbility> findBySuffixItemId(Integer suffixItemId);

	/* ===================== Insert / Update ===================== */

	Integer insert(SuffixItemAbility ability);

	void update(SuffixItemAbility ability);

	/* ===================== Delete ===================== */

	void delete(Integer abilityId);
	
	void deleteBySuffixItemId(Integer suffixItemId);
}
