package com.dnc.simulator.repository;

import java.util.List;
import com.dnc.simulator.model.SuffixItemExtraStat;

public interface SuffixItemExtraStatRepository {

	/* ===== query ===== */

	List<SuffixItemExtraStat> findBySuffixItemId(Integer suffixItemId);

	/* ===== insert / update ===== */

	Integer insert(SuffixItemExtraStat stat);

	void update(SuffixItemExtraStat stat);

	/* ===== delete (ของเดิม) ===== */

	void delete(Integer id);

	/* ===== delete (เพิ่มใหม่ – ใช้กับ save-form) ===== */

	void deleteBySuffixItemId(Integer suffixItemId);
}
