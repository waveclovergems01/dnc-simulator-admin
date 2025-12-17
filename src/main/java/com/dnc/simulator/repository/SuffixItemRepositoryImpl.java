package com.dnc.simulator.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.dnc.simulator.model.SuffixItem;
import com.dnc.simulator.model.SuffixItemAbility;
import com.dnc.simulator.model.SuffixItemExtraStat;

@Repository
public class SuffixItemRepositoryImpl implements SuffixItemRepository {

	private final JdbcTemplate jdbcTemplate;

	public SuffixItemRepositoryImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<SuffixItem> findAll() {

		String sql = "SELECT id, item_id, suffix_type_id, name, tier " + "FROM m_suffix_items "
				+ "ORDER BY item_id, suffix_type_id";

		return jdbcTemplate.query(sql, (rs, i) -> {
			SuffixItem s = new SuffixItem();
			s.setId(rs.getLong("id"));
			s.setItemId(rs.getLong("item_id"));
			s.setSuffixTypeId(rs.getInt("suffix_type_id"));
			s.setName(rs.getString("name"));
			s.setTier(rs.getInt("tier"));
			return s;
		});
	}

	@Override
	public SuffixItem findById(Long id) {

		String sql = "SELECT id, item_id, suffix_type_id, name, tier " + "FROM m_suffix_items " + "WHERE id = ?";

		SuffixItem result = jdbcTemplate.queryForObject(sql, (rs, i) -> {
			SuffixItem s = new SuffixItem();
			s.setId(rs.getLong("id"));
			s.setItemId(rs.getLong("item_id"));
			s.setSuffixTypeId(rs.getInt("suffix_type_id"));
			s.setName(rs.getString("name"));
			s.setTier(rs.getInt("tier"));
			return s;
		}, id);

		if (result.getId() != null && result.getId().longValue() > 0) {
			List<SuffixItemExtraStat> extraStats = new SuffixItemExtraStatRepositoryImpl(jdbcTemplate)
					.findBySuffixItemId(result.getId().intValue());
			result.setExtraStats(extraStats);

			List<SuffixItemAbility> abilities = new SuffixItemAbilityRepositoryImpl(jdbcTemplate)
					.findBySuffixItemId(result.getId().intValue());
			result.setAbilities(abilities);
		}

		return result;
	}

	@Override
	public List<SuffixItem> findByItemId(Long itemId) {

		String sql = "SELECT id, item_id, suffix_type_id, name, tier " + "FROM m_suffix_items " + "WHERE item_id = ? "
				+ "ORDER BY suffix_type_id";

		return jdbcTemplate.query(sql, (rs, i) -> {
			SuffixItem s = new SuffixItem();
			s.setId(rs.getLong("id"));
			s.setItemId(rs.getLong("item_id"));
			s.setSuffixTypeId(rs.getInt("suffix_type_id"));
			s.setName(rs.getString("name"));
			s.setTier(rs.getInt("tier"));
			return s;
		}, itemId);
	}

	@Override
	public void save(SuffixItem item) {

		if (item.getId() == null) {

			// INSERT (new suffix for this item)
			String sql = "INSERT INTO m_suffix_items (item_id, suffix_type_id, name, tier) " + "VALUES (?, ?, ?, ?)";

			jdbcTemplate.update(sql, item.getItemId(), item.getSuffixTypeId(), item.getName(), item.getTier());

		} else {

			// UPDATE (existing suffix)
			String sql = "UPDATE m_suffix_items " + "SET suffix_type_id = ?, name = ? " + "WHERE id = ?";

			jdbcTemplate.update(sql, item.getSuffixTypeId(), item.getName(), item.getId());
		}
	}

	@Override
	public Long saveAndReturnId(SuffixItem item) {

		if (item.getId() == null) {

			String sql = "INSERT INTO m_suffix_items (item_id, suffix_type_id, name, tier) " + "VALUES (?, ?, ?, ?)";

			KeyHolder keyHolder = new GeneratedKeyHolder();

			jdbcTemplate.update(connection -> {
				PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setLong(1, item.getItemId());
				ps.setInt(2, item.getSuffixTypeId());
				ps.setString(3, item.getName());
				ps.setInt(4, item.getTier());
				return ps;
			}, keyHolder);

			return keyHolder.getKey().longValue();

		} else {

			String sql = "UPDATE m_suffix_items " + "SET suffix_type_id = ?, name = ? " + "WHERE id = ?";

			jdbcTemplate.update(sql, item.getSuffixTypeId(), item.getName(), item.getId());

			return item.getId();
		}
	}

	@Override
	public void delete(Long id) {

		String sql = "DELETE FROM m_suffix_items WHERE id = ?";

		jdbcTemplate.update(sql, id);
	}
}
