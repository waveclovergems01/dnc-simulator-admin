package com.dnc.simulator.repository;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.dnc.simulator.repository.RarityRuleRepository;

@Repository
public class RarityRuleRepositoryImpl implements RarityRuleRepository {

	private final JdbcTemplate jdbcTemplate;

	public RarityRuleRepositoryImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<Integer> findRarityIdsByCategory(int categoryId) {
		return jdbcTemplate.queryForList("SELECT rarity_id FROM m_rarity_rules_categories WHERE category_id = ?",
				Integer.class, categoryId);
	}

	@Override
	public List<Integer> findRarityIdsByItemType(int typeId) {
		return jdbcTemplate.queryForList("SELECT rarity_id FROM m_rarity_rules_item_types WHERE type_id = ?",
				Integer.class, typeId);
	}

	@Override
	public void insertCategoryRule(int categoryId, int rarityId) {
		jdbcTemplate.update("INSERT INTO m_rarity_rules_categories (category_id, rarity_id) VALUES (?, ?)", categoryId,
				rarityId);
	}

	@Override
	public void insertItemTypeRule(int typeId, int rarityId) {
		jdbcTemplate.update("INSERT INTO m_rarity_rules_item_types (type_id, rarity_id) VALUES (?, ?)", typeId,
				rarityId);
	}

	@Override
	public void deleteCategoryRules(int categoryId) {
		jdbcTemplate.update("DELETE FROM m_rarity_rules_categories WHERE category_id = ?", categoryId);
	}

	@Override
	public void deleteItemTypeRules(int typeId) {
		jdbcTemplate.update("DELETE FROM m_rarity_rules_item_types WHERE type_id = ?", typeId);
	}
	
	@Override
	public void deleteCategoryRulesByRarityId(int rarityId) {
		jdbcTemplate.update("DELETE FROM m_rarity_rules_categories WHERE rarity_id = ?", rarityId);
	}
	@Override
	public void deleteItemTypeRulesByRarityId(int rarityId) {
		jdbcTemplate.update("DELETE FROM m_rarity_rules_item_types WHERE rarity_id = ?", rarityId);
	}

	@Override
	public List<Integer> findCategoryIdsByRarity(int rarityId) {
		return jdbcTemplate.queryForList("SELECT category_id FROM m_rarity_rules_categories WHERE rarity_id = ?",
				Integer.class, rarityId);
	}

	@Override
	public List<Integer> findItemTypeIdsByRarity(int rarityId) {
		return jdbcTemplate.queryForList("SELECT type_id FROM m_rarity_rules_item_types WHERE rarity_id = ?",
				Integer.class, rarityId);
	}
}
