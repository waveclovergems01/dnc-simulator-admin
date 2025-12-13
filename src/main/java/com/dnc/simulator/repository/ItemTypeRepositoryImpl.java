package com.dnc.simulator.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.dnc.simulator.model.ItemType;
import com.dnc.simulator.repository.ItemTypeRepository;

@Repository
public class ItemTypeRepositoryImpl implements ItemTypeRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private static final String TABLE_NAME = "m_item_types";

	// =========================
	// RowMapper
	// =========================
	private static class ItemTypeRowMapper implements RowMapper<ItemType> {

		@Override
		public ItemType mapRow(ResultSet rs, int rowNum) throws SQLException {
			ItemType itemType = new ItemType();
			itemType.setTypeId(rs.getInt("type_id"));
			itemType.setTypeName(rs.getString("type_name"));
			itemType.setSlot(rs.getString("slot"));
			itemType.setCategoryId(rs.getInt("category_id"));
			return itemType;
		}
	}

	// =========================
	// SELECT
	// =========================
	@Override
	public List<ItemType> findAll() {
		String sql = "SELECT type_id, type_name, slot, category_id FROM " + TABLE_NAME + " ORDER BY type_id";
		return jdbcTemplate.query(sql, new ItemTypeRowMapper());
	}

	@Override
	public ItemType findById(int typeId) {
		String sql = "SELECT type_id, type_name, slot, category_id FROM " + TABLE_NAME + " WHERE type_id = ?";

		List<ItemType> list = jdbcTemplate.query(sql, new ItemTypeRowMapper(), typeId);
		return list.isEmpty() ? null : list.get(0);
	}

	// =========================
	// INSERT
	// =========================
	@Override
	public void insert(ItemType itemType) {
		String sql = "INSERT INTO " + TABLE_NAME + " (type_id, type_name, slot, category_id) " + " VALUES (?, ?, ?, ?)";

		jdbcTemplate.update(sql, itemType.getTypeId(), itemType.getTypeName(), itemType.getSlot(),
				itemType.getCategoryId());
	}

	// =========================
	// UPDATE
	// =========================
	@Override
	public void update(ItemType itemType) {
		String sql = "UPDATE " + TABLE_NAME + " SET type_name = ?, slot = ?, category_id = ? " + " WHERE type_id = ?";

		jdbcTemplate.update(sql, itemType.getTypeName(), itemType.getSlot(), itemType.getCategoryId(),
				itemType.getTypeId());
	}

	// =========================
	// DELETE
	// =========================
	@Override
	public void delete(int typeId) {
		String sql = "DELETE FROM " + TABLE_NAME + " WHERE type_id = ?";
		jdbcTemplate.update(sql, typeId);
	}
}
