package com.dnc.simulator.repository;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.dnc.simulator.model.*;
import com.dnc.simulator.repository.EquipmentItemRepository;

@Repository
public class EquipmentItemRepositoryImpl implements EquipmentItemRepository {

	private final JdbcTemplate jdbcTemplate;

	public EquipmentItemRepositoryImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<EquipmentItem> findAll() {
		String sql = "SELECT item_id, name, type_id, job_id, required_level, " + "rarity_id, durability, set_id "
				+ "FROM m_equipment_items ORDER BY item_id";

		return jdbcTemplate.query(sql, (rs, i) -> {
			EquipmentItem e = new EquipmentItem();
			e.setItemId(rs.getLong("item_id"));
			e.setName(rs.getString("name"));
			e.setTypeId(rs.getInt("type_id"));
			e.setJobId(rs.getInt("job_id"));
			e.setRequiredLevel(rs.getInt("required_level"));
			e.setRarityId(rs.getInt("rarity_id"));
			e.setDurability(rs.getInt("durability"));
			e.setSetId(rs.getObject("set_id") != null ? rs.getInt("set_id") : null);
			return e;
		});
	}

	@Override
	public EquipmentItem findById(Long itemId) {
		String sql = "SELECT item_id, name, type_id, job_id, required_level, " + "rarity_id, durability, set_id "
				+ "FROM m_equipment_items WHERE item_id = ?";

		EquipmentItem item = jdbcTemplate.queryForObject(sql, (rs, i) -> {
			EquipmentItem e = new EquipmentItem();
			e.setItemId(rs.getLong("item_id"));
			e.setName(rs.getString("name"));
			e.setTypeId(rs.getInt("type_id"));
			e.setJobId(rs.getInt("job_id"));
			e.setRequiredLevel(rs.getInt("required_level"));
			e.setRarityId(rs.getInt("rarity_id"));
			e.setDurability(rs.getInt("durability"));
			e.setSetId(rs.getObject("set_id") != null ? rs.getInt("set_id") : null);
			return e;
		}, itemId);

		item.setStats(findStatsByItemId(itemId));
		return item;
	}

	@Override
	public void insert(EquipmentItem item) {
		String sql = "INSERT INTO m_equipment_items "
				+ "(item_id, name, type_id, job_id, required_level, rarity_id, durability, set_id) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

		jdbcTemplate.update(sql, item.getItemId(), item.getName(), item.getTypeId(), item.getJobId(),
				item.getRequiredLevel(), item.getRarityId(), item.getDurability(), item.getSetId());
	}

	@Override
	public void update(EquipmentItem item) {
		String sql = "UPDATE m_equipment_items SET " + "name = ?, type_id = ?, job_id = ?, required_level = ?, "
				+ "rarity_id = ?, durability = ?, set_id = ? " + "WHERE item_id = ?";

		jdbcTemplate.update(sql, item.getName(), item.getTypeId(), item.getJobId(), item.getRequiredLevel(),
				item.getRarityId(), item.getDurability(), item.getSetId(), item.getItemId());
	}

	@Override
	public void delete(Long itemId) {
		jdbcTemplate.update("DELETE FROM m_equipment_items WHERE item_id = ?", itemId);
	}

	/* ================= STATS ================= */

	@Override
	public List<EquipmentItemStat> findStatsByItemId(Long itemId) {
		String sql = "SELECT item_id, stat_id, value_min, value_max, is_percentage "
				+ "FROM m_equipment_item_stats WHERE item_id = ?";

		return jdbcTemplate.query(sql, (rs, i) -> {
			EquipmentItemStat s = new EquipmentItemStat();
			s.setItemId(rs.getLong("item_id"));
			s.setStatId(rs.getInt("stat_id"));
			s.setValueMin(rs.getInt("value_min"));
			s.setValueMax(rs.getInt("value_max"));
			s.setIsPercentage(rs.getInt("is_percentage"));
			return s;
		}, itemId);
	}

	@Override
	public void insertStat(EquipmentItemStat stat) {
		String sql = "INSERT INTO m_equipment_item_stats " + "(item_id, stat_id, value_min, value_max, is_percentage) "
				+ "VALUES (?, ?, ?, ?, ?)";

		jdbcTemplate.update(sql, stat.getItemId(), stat.getStatId(), stat.getValueMin(), stat.getValueMax(),
				stat.getIsPercentage());
	}

	@Override
	public void deleteStatsByItemId(Long itemId) {
		jdbcTemplate.update("DELETE FROM m_equipment_item_stats WHERE item_id = ?", itemId);
	}
}
