package com.dnc.simulator.service;

import java.util.List;

import com.dnc.simulator.model.SuffixItemAbility;
import com.dnc.simulator.model.SuffixItemAbilityStat;
import com.dnc.simulator.model.SuffixItemExtraStat;

public interface SuffixItemStatService {

	/* ===================== Ability ===================== */

	List<SuffixItemAbility> getAbilities(Integer suffixItemId);

	SuffixItemAbility getAbilityWithStats(Integer abilityId);

	List<SuffixItemAbility> getAbilitiesWithStatsBySuffixItemId(Integer suffixItemId);

	Integer saveAbility(SuffixItemAbility ability);

	void deleteAbility(Integer abilityId);

	/* ===================== Ability Stat ===================== */

	List<SuffixItemAbilityStat> getAbilityStats(Integer abilityId);

	Integer saveAbilityStat(SuffixItemAbilityStat stat);

	void deleteAbilityStat(Integer id);

	/* ===================== Extra Stat ===================== */

	List<SuffixItemExtraStat> getExtraStats(Integer suffixItemId);

	Integer saveExtraStat(SuffixItemExtraStat stat);

	void deleteExtraStat(Integer id);

	/* ===================== Extra Stat (เพิ่มใหม่) ===================== */

	/**
	 * ลบ extra stats ทั้งหมดของ suffix item (ใช้ตอน save-form)
	 */
	void deleteExtraStatsBySuffixItemId(Integer suffixItemId);

	/**
	 * สร้าง default extra stats จาก suffix type (ยังไม่ save ลง DB)
	 */
	List<SuffixItemExtraStat> buildDefaultExtraStats(Integer suffixTypeId);
}
