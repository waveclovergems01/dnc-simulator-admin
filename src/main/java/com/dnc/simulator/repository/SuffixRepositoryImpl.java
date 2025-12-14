package com.dnc.simulator.repository;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.dnc.simulator.model.*;
import com.dnc.simulator.repository.SuffixRepository;

@Repository
public class SuffixRepositoryImpl implements SuffixRepository {

	private final JdbcTemplate jdbcTemplate;

	public SuffixRepositoryImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/* ===================== Suffix Types ===================== */

	@Override
	public List<SuffixType> findAllSuffixTypes() {
		String sql = "SELECT suffix_id, suffix_name " + "FROM m_suffix_types " + "ORDER BY suffix_id";

		return jdbcTemplate.query(sql, (rs, i) -> {
			SuffixType s = new SuffixType();
			s.setSuffixId(rs.getInt("suffix_id"));
			s.setSuffixName(rs.getString("suffix_name"));
			return s;
		});
	}

	@Override
	public SuffixType findSuffixTypeById(Integer suffixId) {
		String sql = "SELECT suffix_id, suffix_name " + "FROM m_suffix_types " + "WHERE suffix_id = ?";

		return jdbcTemplate.queryForObject(sql, (rs, i) -> {
			SuffixType s = new SuffixType();
			s.setSuffixId(rs.getInt("suffix_id"));
			s.setSuffixName(rs.getString("suffix_name"));
			return s;
		}, suffixId);
	}

	@Override
	public void insertSuffixType(SuffixType suffixType) {
		String sql = "INSERT INTO m_suffix_types (suffix_id, suffix_name) " + "VALUES (?, ?)";

		jdbcTemplate.update(sql, suffixType.getSuffixId(), suffixType.getSuffixName());
	}

	@Override
	public void updateSuffixType(SuffixType suffixType) {
		String sql = "UPDATE m_suffix_types " + "SET suffix_name = ? " + "WHERE suffix_id = ?";

		jdbcTemplate.update(sql, suffixType.getSuffixName(), suffixType.getSuffixId());
	}

	@Override
	public void deleteSuffixType(Integer suffixId) {
		jdbcTemplate.update("DELETE FROM m_suffix_types WHERE suffix_id = ?", suffixId);
	}

	/* ===================== Suffix Groups ===================== */

	@Override
	public List<SuffixGroup> findAllGroups() {
		String sql = "SELECT group_id, item_type_id " + "FROM m_suffix_groups " + "ORDER BY group_id";

		return jdbcTemplate.query(sql, (rs, i) -> {
			SuffixGroup g = new SuffixGroup();
			g.setGroupId(rs.getInt("group_id"));
			g.setItemTypeId(rs.getInt("item_type_id"));
			return g;
		});
	}

	@Override
	public SuffixGroup findGroupById(Integer groupId) {
		String sql = "SELECT group_id, item_type_id " + "FROM m_suffix_groups " + "WHERE group_id = ?";

		return jdbcTemplate.queryForObject(sql, (rs, i) -> {
			SuffixGroup g = new SuffixGroup();
			g.setGroupId(rs.getInt("group_id"));
			g.setItemTypeId(rs.getInt("item_type_id"));
			return g;
		}, groupId);
	}

	@Override
	public void insertGroup(SuffixGroup group) {
		String sql = "INSERT INTO m_suffix_groups (group_id, item_type_id) " + "VALUES (?, ?)";

		jdbcTemplate.update(sql, group.getGroupId(), group.getItemTypeId());
	}

	@Override
	public void updateGroup(SuffixGroup group) {
		String sql = "UPDATE m_suffix_groups " + "SET item_type_id = ? " + "WHERE group_id = ?";

		jdbcTemplate.update(sql, group.getItemTypeId(), group.getGroupId());
	}

	@Override
	public void deleteGroup(Integer groupId) {
		jdbcTemplate.update("DELETE FROM m_suffix_groups WHERE group_id = ?", groupId);
	}

	/* ===================== Suffix Group Items ===================== */

	@Override
	public List<SuffixGroupItem> findAllGroupItems() {
		String sql = "SELECT id, group_id, suffix_id, mode " + "FROM m_suffix_group_items";

		return jdbcTemplate.query(sql, (rs, i) -> {
			SuffixGroupItem item = new SuffixGroupItem();
			item.setId(rs.getInt("id"));
			item.setGroupId(rs.getInt("group_id"));
			item.setSuffixId(rs.getInt("suffix_id"));
			item.setMode(rs.getString("mode"));
			return item;
		});
	}

	@Override
	public List<SuffixGroupItem> findGroupItemsByGroupId(Integer groupId) {
		String sql = "SELECT id, group_id, suffix_id, mode " + "FROM m_suffix_group_items " + "WHERE group_id = ? "
				+ "ORDER BY mode, suffix_id";

		return jdbcTemplate.query(sql, (rs, i) -> {
			SuffixGroupItem item = new SuffixGroupItem();
			item.setId(rs.getInt("id"));
			item.setGroupId(rs.getInt("group_id"));
			item.setSuffixId(rs.getInt("suffix_id"));
			item.setMode(rs.getString("mode"));
			return item;
		}, groupId);
	}

	@Override
	public SuffixGroupItem findGroupItemById(Integer id) {
		String sql = "SELECT id, group_id, suffix_id, mode " + "FROM m_suffix_group_items " + "WHERE id = ?";

		return jdbcTemplate.queryForObject(sql, (rs, i) -> {
			SuffixGroupItem item = new SuffixGroupItem();
			item.setId(rs.getInt("id"));
			item.setGroupId(rs.getInt("group_id"));
			item.setSuffixId(rs.getInt("suffix_id"));
			item.setMode(rs.getString("mode"));
			return item;
		}, id);
	}

	@Override
	public void insertGroupItem(SuffixGroupItem item) {
		String sql = "INSERT INTO m_suffix_group_items (group_id, suffix_id, mode) " + "VALUES (?, ?, ?)";

		jdbcTemplate.update(sql, item.getGroupId(), item.getSuffixId(), item.getMode());
	}

	@Override
	public void updateGroupItem(SuffixGroupItem item) {
		String sql = "UPDATE m_suffix_group_items " + "SET group_id = ?, suffix_id = ?, mode = ? " + "WHERE id = ?";

		jdbcTemplate.update(sql, item.getGroupId(), item.getSuffixId(), item.getMode(), item.getId());
	}

	@Override
	public void deleteGroupItem(Integer id) {
		jdbcTemplate.update("DELETE FROM m_suffix_group_items WHERE id = ?", id);
	}
}
