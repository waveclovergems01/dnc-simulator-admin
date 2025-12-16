package com.dnc.simulator.repository;

import java.util.List;
import com.dnc.simulator.model.SuffixItemAbilityStat;

public interface SuffixItemAbilityStatRepository {

	/* ===================== Query ===================== */

	List<SuffixItemAbilityStat> findByAbilityId(Integer abilityId);

	/* ===================== Insert / Update ===================== */

	Integer insert(SuffixItemAbilityStat stat);

	void update(SuffixItemAbilityStat stat);

	/* ===================== Delete ===================== */

	void delete(Integer id);

	/* ===================== Delete (เพิ่มใหม่) ===================== */

	void deleteByAbilityId(Integer abilityId);
}
