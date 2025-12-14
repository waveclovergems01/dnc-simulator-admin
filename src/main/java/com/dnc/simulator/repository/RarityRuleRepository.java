package com.dnc.simulator.repository;

import java.util.List;

public interface RarityRuleRepository {

	List<Integer> findRarityIdsByCategory(int categoryId);

	List<Integer> findRarityIdsByItemType(int typeId);

	void insertCategoryRule(int categoryId, int rarityId);

	void insertItemTypeRule(int typeId, int rarityId);

	void deleteCategoryRules(int categoryId);

	void deleteItemTypeRules(int typeId);
	
	public void deleteCategoryRulesByRarityId(int rarityId);

	public void deleteItemTypeRulesByRarityId(int rarityId);
	
	public List<Integer> findCategoryIdsByRarity(int rarityId);

	public List<Integer> findItemTypeIdsByRarity(int rarityId);
}
