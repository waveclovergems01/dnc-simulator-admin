package com.dnc.simulator.repository.equipment;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.dnc.simulator.model.EquipmentItemStat;
import com.dnc.simulator.model.equipment.EquipmentItem;

@Repository
public class EquipmentCloneRepositoryImpl implements EquipmentCloneRepository {

	private final JdbcTemplate jdbcTemplate;

	public EquipmentCloneRepositoryImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@PostConstruct
	public void init() {
		ensureTable();
	}

	@Override
	public void ensureTable() {
		createEquipmentItemTableIfNotExists();
		createEquipmentItemStatsTableIfNotExists();
		ensureEquipmentItemColumns();
	}

	private void createEquipmentItemTableIfNotExists() {
		String sql = "" + "CREATE TABLE IF NOT EXISTS m_equipment_items (" + " item_id INTEGER PRIMARY KEY,"
				+ " name TEXT NOT NULL," + " type_id INTEGER NOT NULL," + " job_id INTEGER,"
				+ " required_level INTEGER," + " rarity_id INTEGER," + " durability INTEGER," + " set_id INTEGER,"
				+ " icon_blob BLOB," + " icon_mime TEXT," + " icon_name TEXT" + ")";
		jdbcTemplate.execute(sql);
	}

	private void createEquipmentItemStatsTableIfNotExists() {
		String sql = "" + "CREATE TABLE IF NOT EXISTS m_equipment_item_stats (" + " item_id INTEGER NOT NULL,"
				+ " stat_id INTEGER NOT NULL," + " value_min REAL," + " value_max REAL,"
				+ " is_percentage INTEGER DEFAULT 0" + ")";
		jdbcTemplate.execute(sql);
	}

	private void ensureEquipmentItemColumns() {
		addColumnIfMissing("m_equipment_items", "icon_blob", "BLOB");
		addColumnIfMissing("m_equipment_items", "icon_mime", "TEXT");
		addColumnIfMissing("m_equipment_items", "icon_name", "TEXT");
	}

	private void addColumnIfMissing(String tableName, String columnName, String columnDefinition) {
		if (!columnExists(tableName, columnName)) {
			String sql = "ALTER TABLE " + tableName + " ADD COLUMN " + columnName + " " + columnDefinition;
			jdbcTemplate.execute(sql);
		}
	}

	private boolean columnExists(String tableName, String columnName) {
		try {
			List<String> columns = jdbcTemplate.query("PRAGMA table_info(" + tableName + ")",
					(rs, rowNum) -> rs.getString("name"));

			for (String col : columns) {
				if (columnName.equalsIgnoreCase(col)) {
					return true;
				}
			}
		} catch (Exception e) {
		}

		return false;
	}

	@Override
	public List<EquipmentItem> findItemsBySetId(Long setId) {
		ensureTable();

		String sql = "" + "SELECT item_id, name, type_id, job_id, required_level, rarity_id, durability, set_id, "
				+ "       icon_blob, icon_mime, icon_name " + "FROM m_equipment_items " + "WHERE set_id = ? "
				+ "ORDER BY item_id";

		return jdbcTemplate.query(sql, (rs, i) -> {
			EquipmentItem e = new EquipmentItem();
			e.setItemId(rs.getLong("item_id"));
			e.setName(rs.getString("name"));
			e.setTypeId(rs.getInt("type_id"));

			Object jobIdObj = rs.getObject("job_id");
			if (jobIdObj != null) {
				e.setJobId(rs.getInt("job_id"));
			}

			Object requiredLevelObj = rs.getObject("required_level");
			if (requiredLevelObj != null) {
				e.setRequiredLevel(rs.getInt("required_level"));
			}

			Object rarityIdObj = rs.getObject("rarity_id");
			if (rarityIdObj != null) {
				e.setRarityId(rs.getInt("rarity_id"));
			}

			Object durabilityObj = rs.getObject("durability");
			if (durabilityObj != null) {
				e.setDurability(rs.getInt("durability"));
			}

			Object setIdObj = rs.getObject("set_id");
			if (setIdObj != null) {
				e.setSetId(rs.getInt("set_id"));
			}

			e.setIconBlob(rs.getBytes("icon_blob"));
			e.setIconMime(rs.getString("icon_mime"));
			e.setIconName(rs.getString("icon_name"));

			return e;
		}, setId);
	}

	@Override
	public List<EquipmentItemStat> findStatsByItemId(Long itemId) {
		ensureTable();

		String sql = "" + "SELECT item_id, stat_id, value_min, value_max, is_percentage "
				+ "FROM m_equipment_item_stats " + "WHERE item_id = ?";

		return jdbcTemplate.query(sql, (rs, i) -> {
			EquipmentItemStat s = new EquipmentItemStat();
			s.setItemId(rs.getLong("item_id"));
			s.setStatId(rs.getInt("stat_id"));

			Object valueMinObj = rs.getObject("value_min");
			if (valueMinObj != null) {
				s.setValueMin(rs.getDouble("value_min"));
			}

			Object valueMaxObj = rs.getObject("value_max");
			if (valueMaxObj != null) {
				s.setValueMax(rs.getDouble("value_max"));
			}

			Object isPercentageObj = rs.getObject("is_percentage");
			if (isPercentageObj != null) {
				s.setIsPercentage(rs.getInt("is_percentage"));
			} else {
				s.setIsPercentage(0);
			}

			return s;
		}, itemId);
	}

	@Override
	public void insertEquipmentItem(EquipmentItem item) {
		ensureTable();

		String sql = "" + "INSERT INTO m_equipment_items "
				+ "(item_id, name, type_id, job_id, required_level, rarity_id, durability, set_id, icon_blob, icon_mime, icon_name) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		jdbcTemplate.update(sql, item.getItemId(), item.getName(), item.getTypeId(), item.getJobId(),
				item.getRequiredLevel(), item.getRarityId(), item.getDurability(), item.getSetId(), item.getIconBlob(),
				item.getIconMime(), item.getIconName());
	}

	@Override
	public void insertEquipmentItemStat(EquipmentItemStat stat) {
		ensureTable();

		String sql = "" + "INSERT INTO m_equipment_item_stats "
				+ "(item_id, stat_id, value_min, value_max, is_percentage) " + "VALUES (?, ?, ?, ?, ?)";

		jdbcTemplate.update(sql, stat.getItemId(), stat.getStatId(), stat.getValueMin(), stat.getValueMax(),
				stat.getIsPercentage());
	}

	@Override
	public boolean existsItemId(Long itemId) {
		ensureTable();

		String sql = "SELECT COUNT(1) FROM m_equipment_items WHERE item_id = ?";

		Integer cnt = jdbcTemplate.queryForObject(sql, Integer.class, itemId);
		return cnt != null && cnt.intValue() > 0;
	}
}