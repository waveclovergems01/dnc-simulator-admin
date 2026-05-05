package com.dnc.simulator.repository.card;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.dnc.simulator.model.card.CardName;

@Repository
public class CardNameRepositoryImpl implements CardNameRepository {

	private final JdbcTemplate jdbcTemplate;

	public CardNameRepositoryImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@PostConstruct
	public void init() {
		ensureTable();
	}

	@Override
	public void ensureTable() {
		createTableIfNotExists();
		ensureIconColumns();
		ensureUniqueIndex();
	}

	private void createTableIfNotExists() {
		String sql = "" + "CREATE TABLE IF NOT EXISTS m_card_name (" + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ " name TEXT NOT NULL," + " icon_blob BLOB," + " icon_mime TEXT," + " icon_name TEXT" + ")";
		jdbcTemplate.execute(sql);
	}

	private void ensureIconColumns() {
		if (!columnExists("m_card_name", "icon_blob")) {
			jdbcTemplate.execute("ALTER TABLE m_card_name ADD COLUMN icon_blob BLOB");
		}

		if (!columnExists("m_card_name", "icon_mime")) {
			jdbcTemplate.execute("ALTER TABLE m_card_name ADD COLUMN icon_mime TEXT");
		}

		if (!columnExists("m_card_name", "icon_name")) {
			jdbcTemplate.execute("ALTER TABLE m_card_name ADD COLUMN icon_name TEXT");
		}
	}

	private void ensureUniqueIndex() {
		try {
			String sql = "" + "CREATE UNIQUE INDEX IF NOT EXISTS uk_m_card_name_name " + "ON m_card_name (name)";
			jdbcTemplate.execute(sql);
		} catch (Exception e) {
			throw new RuntimeException(
					"Cannot create unique index for m_card_name. Please remove duplicate Card Name data first.", e);
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
	public List<CardName> findAll() {
		String sql = "" + "SELECT id, name, icon_blob, icon_mime, icon_name " + "FROM m_card_name "
				+ "ORDER BY name ASC";

		return jdbcTemplate.query(sql, cardNameRowMapper());
	}

	@Override
	public Optional<CardName> findById(Long id) {
		String sql = "" + "SELECT id, name, icon_blob, icon_mime, icon_name " + "FROM m_card_name " + "WHERE id = ?";

		List<CardName> list = jdbcTemplate.query(sql, cardNameRowMapper(), id);
		return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
	}

	@Override
	public Optional<CardName> findIconById(Long id) {
		String sql = "" + "SELECT id, icon_blob, icon_mime, icon_name " + "FROM m_card_name " + "WHERE id = ?";

		List<CardName> list = jdbcTemplate.query(sql, (rs, rowNum) -> {
			CardName x = new CardName();
			x.setId(rs.getLong("id"));
			x.setIconBlob(rs.getBytes("icon_blob"));
			x.setIconMime(rs.getString("icon_mime"));
			x.setIconName(rs.getString("icon_name"));
			return x;
		}, id);

		return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
	}

	@Override
	public boolean existsByName(String name, Long excludeId) {
		String sql = "" + "SELECT COUNT(1) " + "FROM m_card_name " + "WHERE TRIM(LOWER(name)) = TRIM(LOWER(?)) ";

		Integer count;

		if (excludeId != null) {
			sql += " AND id <> ? ";
			count = jdbcTemplate.queryForObject(sql, Integer.class, name, excludeId);
		} else {
			count = jdbcTemplate.queryForObject(sql, Integer.class, name);
		}

		return count != null && count.intValue() > 0;
	}

	@Override
	public Long insert(String name, byte[] iconBlob, String iconMime, String iconName) {
		String sql = "" + "INSERT INTO m_card_name " + " (name, icon_blob, icon_mime, icon_name) "
				+ "VALUES (?, ?, ?, ?)";

		KeyHolder keyHolder = new GeneratedKeyHolder();

		jdbcTemplate.update(con -> {
			PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			ps.setString(1, name);

			if (iconBlob != null) {
				ps.setBytes(2, iconBlob);
			} else {
				ps.setNull(2, Types.BLOB);
			}

			if (iconMime != null) {
				ps.setString(3, iconMime);
			} else {
				ps.setNull(3, Types.VARCHAR);
			}

			if (iconName != null) {
				ps.setString(4, iconName);
			} else {
				ps.setNull(4, Types.VARCHAR);
			}

			return ps;
		}, keyHolder);

		return keyHolder.getKey() == null ? null : keyHolder.getKey().longValue();
	}

	@Override
	public int update(Long id, String name, byte[] iconBlob, String iconMime, String iconName) {
		String sql = "" + "UPDATE m_card_name " + "SET name = ?, " + "    icon_blob = ?, " + "    icon_mime = ?, "
				+ "    icon_name = ? " + "WHERE id = ?";

		return jdbcTemplate.update(con -> {
			PreparedStatement ps = con.prepareStatement(sql);

			ps.setString(1, name);

			if (iconBlob != null) {
				ps.setBytes(2, iconBlob);
			} else {
				ps.setNull(2, Types.BLOB);
			}

			if (iconMime != null) {
				ps.setString(3, iconMime);
			} else {
				ps.setNull(3, Types.VARCHAR);
			}

			if (iconName != null) {
				ps.setString(4, iconName);
			} else {
				ps.setNull(4, Types.VARCHAR);
			}

			ps.setLong(5, id);

			return ps;
		});
	}

	@Override
	public int updateNoIcon(Long id, String name) {
		String sql = "" + "UPDATE m_card_name " + "SET name = ? " + "WHERE id = ?";

		return jdbcTemplate.update(sql, name, id);
	}

	@Override
	public int delete(Long id) {
		String sql = "DELETE FROM m_card_name WHERE id = ?";
		return jdbcTemplate.update(sql, id);
	}

	private RowMapper<CardName> cardNameRowMapper() {
		return (rs, rowNum) -> {
			CardName x = new CardName();

			x.setId(rs.getLong("id"));
			x.setName(rs.getString("name"));

			try {
				x.setIconBlob(rs.getBytes("icon_blob"));
			} catch (Exception e) {
			}

			try {
				x.setIconMime(rs.getString("icon_mime"));
			} catch (Exception e) {
			}

			try {
				x.setIconName(rs.getString("icon_name"));
			} catch (Exception e) {
			}

			return x;
		};
	}
}