package com.dnc.simulator.service;

import java.util.List;
import com.dnc.simulator.model.Rarity;

public interface RarityService {

	List<Rarity> getAllRarities();

	Rarity getRarityById(int id);

	void saveRarity(Rarity rarity, boolean isAdd);

	void deleteRarity(int id);

	List<Integer> getRaritiesByCategory(int categoryId);

	List<Integer> getRaritiesByItemType(int typeId);

	void saveCategoryRarities(List<Integer> categoryIds, int rarityId);

	void saveItemTypeRarities(List<Integer> typeIds, int rarityIds);

	List<Integer> getCategoriesByRarity(int rarityId);

	List<Integer> getItemTypesByRarity(int rarityId);

}
