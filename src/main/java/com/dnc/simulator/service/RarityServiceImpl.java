package com.dnc.simulator.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dnc.simulator.model.Rarity;
import com.dnc.simulator.repository.RarityRepository;
import com.dnc.simulator.repository.RarityRuleRepository;
import com.dnc.simulator.service.RarityService;

@Service
public class RarityServiceImpl implements RarityService {

	private final RarityRepository rarityRepository;
	private final RarityRuleRepository ruleRepository;

	public RarityServiceImpl(RarityRepository rarityRepository, RarityRuleRepository ruleRepository) {
		this.rarityRepository = rarityRepository;
		this.ruleRepository = ruleRepository;
	}

	@Override
	public List<Rarity> getAllRarities() {
		return rarityRepository.findAll();
	}

	@Override
	public Rarity getRarityById(int id) {
		return rarityRepository.findById(id);
	}

	@Override
	public void saveRarity(Rarity rarity, boolean isAdd) {
		if (isAdd) {
			rarityRepository.insert(rarity);
		} else {
			rarityRepository.update(rarity);
		}
	}

	@Override
	public void deleteRarity(int id) {
		ruleRepository.deleteCategoryRulesByRarityId(id);
		ruleRepository.deleteItemTypeRulesByRarityId(id);
		rarityRepository.delete(id);
	}

	@Override
	public List<Integer> getRaritiesByCategory(int categoryId) {
		return ruleRepository.findRarityIdsByCategory(categoryId);
	}

	@Override
	public List<Integer> getRaritiesByItemType(int typeId) {
		return ruleRepository.findRarityIdsByItemType(typeId);
	}

	@Transactional
	@Override
	public void saveCategoryRarities(List<Integer> categoryIds, int rarityId) {
		ruleRepository.deleteCategoryRulesByRarityId(rarityId);
		for (int categoryId : categoryIds) {
			ruleRepository.insertCategoryRule(categoryId, rarityId);
		}
	}

	@Transactional
	@Override
	public void saveItemTypeRarities(List<Integer> typeIds, int rarityId) {
		ruleRepository.deleteItemTypeRulesByRarityId(rarityId);
		for (int typeId : typeIds) {
			ruleRepository.insertItemTypeRule(typeId, rarityId);
		}
	}

	@Override
	public List<Integer> getCategoriesByRarity(int rarityId) {
		return ruleRepository.findCategoryIdsByRarity(rarityId);
	}

	@Override
	public List<Integer> getItemTypesByRarity(int rarityId) {
		return ruleRepository.findItemTypeIdsByRarity(rarityId);
	}

}
