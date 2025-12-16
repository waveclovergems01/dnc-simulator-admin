package com.dnc.simulator.service;

import java.util.List;

import com.dnc.simulator.model.SuffixItemAbility;
import com.dnc.simulator.model.SuffixItemAbilityStat;
import com.dnc.simulator.model.SuffixItemExtraStat;

public interface SuffixItemStatService {

	/*
	 * ===================================================== EXTRA STAT (ของเดิม)
	 * =====================================================
	 */
	List<SuffixItemExtraStat> getExtraStats(Integer suffixItemId);

	Integer saveExtraStat(SuffixItemExtraStat stat);

	void deleteExtraStat(Integer id);

	void deleteExtraStatsBySuffixItemId(Integer suffixItemId);

	List<SuffixItemExtraStat> buildDefaultExtraStats(Integer suffixTypeId);

	/*
	 * ===================================================== ABILITY (ADD)
	 * =====================================================
	 */

	List<SuffixItemAbility> getAbilities(Integer suffixItemId);

	List<SuffixItemAbility> getAbilitiesWithStatsBySuffixItemId(Integer suffixItemId);

	Integer saveAbility(SuffixItemAbility ability);

	void deleteAbility(Integer abilityId);

	/* ================= Ability Stat ================= */

	List<SuffixItemAbilityStat> getAbilityStats(Integer abilityId);

	Integer saveAbilityStat(SuffixItemAbilityStat stat);

	void deleteAbilityStat(Integer id);

	/* ===== IMPORTANT (ADD) ===== */
	void deleteAbilityStatsByAbilityId(Integer abilityId);
	
	void deleteAbilityBySuffixItemId(Integer suffixItemId);
}
