package com.dnc.simulator.repository.plate;

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

import com.dnc.simulator.model.plate.Plate;

@Repository
public class PlateRepositoryImpl implements PlateRepository {

	private final JdbcTemplate jdbcTemplate;

	public PlateRepositoryImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@PostConstruct
	public void init() {
		ensureTable();
	}

	@Override
	public void ensureTable() {
		createTableIfNotExists();
		migrateOldTableStructureIfNeeded();
		ensureUniqueIndex();
	}

	private void createTableIfNotExists() {
		String sql = ""
				+ "CREATE TABLE IF NOT EXISTS m_plate ("
				+ " id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ " type_id INTEGER NOT NULL,"
				+ " plate_level_id INTEGER NOT NULL,"
				+ " plate_name_id INTEGER NOT NULL,"
				+ " rarity_id INTEGER NOT NULL,"
				+ " stat_id INTEGER,"
				+ " stat_value INTEGER,"
				+ " stat_percent REAL"
				+ ")";
		jdbcTemplate.execute(sql);
	}

	private void migrateOldTableStructureIfNeeded() {
		boolean hasPlateNameTextColumn = columnExists("m_plate", "plate_name");
		boolean hasPlateTypeIdColumn = columnExists("m_plate", "plate_type_id");
		boolean hasTypeIdColumn = columnExists("m_plate", "type_id");
		boolean hasPlateNameIdColumn = columnExists("m_plate", "plate_name_id");

		boolean needMigrate = hasPlateNameTextColumn || hasPlateTypeIdColumn || !hasTypeIdColumn || !hasPlateNameIdColumn;

		if (!needMigrate) {
			return;
		}

		jdbcTemplate.execute("DROP TABLE IF EXISTS m_plate_new");

		String createNewSql = ""
				+ "CREATE TABLE m_plate_new ("
				+ " id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ " type_id INTEGER NOT NULL,"
				+ " plate_level_id INTEGER NOT NULL,"
				+ " plate_name_id INTEGER NOT NULL,"
				+ " rarity_id INTEGER NOT NULL,"
				+ " stat_id INTEGER,"
				+ " stat_value INTEGER,"
				+ " stat_percent REAL"
				+ ")";
		jdbcTemplate.execute(createNewSql);

		if (hasPlateNameTextColumn) {
			ensureAllOldPlateNamesExistInMaster();
		}

		String insertSql = buildMigrationInsertSql(hasPlateNameTextColumn, hasPlateTypeIdColumn, hasTypeIdColumn,
				hasPlateNameIdColumn);

		jdbcTemplate.execute(insertSql);

		jdbcTemplate.execute("DROP TABLE m_plate");
		jdbcTemplate.execute("ALTER TABLE m_plate_new RENAME TO m_plate");
	}

	private String buildMigrationInsertSql(boolean hasPlateNameTextColumn, boolean hasPlateTypeIdColumn,
			boolean hasTypeIdColumn, boolean hasPlateNameIdColumn) {

		StringBuilder sql = new StringBuilder();

		sql.append("INSERT INTO m_plate_new ");
		sql.append("(id, type_id, plate_level_id, plate_name_id, rarity_id, stat_id, stat_value, stat_percent) ");

		sql.append("SELECT ");
		sql.append("p.id, ");

		if (hasTypeIdColumn) {
			sql.append("p.type_id AS type_id, ");
		} else if (hasPlateTypeIdColumn) {
			sql.append("p.plate_type_id AS type_id, ");
		} else {
			sql.append("0 AS type_id, ");
		}

		sql.append("p.plate_level_id, ");

		if (hasPlateNameIdColumn) {
			sql.append("p.plate_name_id AS plate_name_id, ");
		} else if (hasPlateNameTextColumn) {
			sql.append("pn.id AS plate_name_id, ");
		} else {
			sql.append("0 AS plate_name_id, ");
		}

		sql.append("p.rarity_id, ");
		sql.append("p.stat_id, ");
		sql.append("p.stat_value, ");
		sql.append("p.stat_percent ");
		sql.append("FROM m_plate p ");

		if (!hasPlateNameIdColumn && hasPlateNameTextColumn) {
			sql.append("INNER JOIN m_plate_name pn ");
			sql.append("ON TRIM(LOWER(pn.name)) = TRIM(LOWER(p.plate_name)) ");
		}

		return sql.toString();
	}

	private void ensureAllOldPlateNamesExistInMaster() {
		String sql = ""
				+ "INSERT INTO m_plate_name (name) "
				+ "SELECT DISTINCT TRIM(p.plate_name) "
				+ "FROM m_plate p "
				+ "WHERE p.plate_name IS NOT NULL "
				+ "  AND TRIM(p.plate_name) <> '' "
				+ "  AND NOT EXISTS ("
				+ "      SELECT 1 "
				+ "      FROM m_plate_name pn "
				+ "      WHERE TRIM(LOWER(pn.name)) = TRIM(LOWER(p.plate_name))"
				+ "  )";
		jdbcTemplate.execute(sql);
	}

	private void ensureUniqueIndex() {
		try {
			jdbcTemplate.execute("DROP INDEX IF EXISTS uk_m_plate_unique_key");
		} catch (Exception e) {
		}

		try {
			String sql = ""
					+ "CREATE UNIQUE INDEX IF NOT EXISTS uk_m_plate_unique_key "
					+ "ON m_plate (type_id, plate_level_id, plate_name_id, rarity_id)";
			jdbcTemplate.execute(sql);
		} catch (Exception e) {
			throw new RuntimeException(
					"Cannot create unique index for m_plate. Please remove duplicate data first.", e);
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
	public List<Plate> findAll() {
		String sql = ""
				+ "SELECT p.id, "
				+ "       p.type_id, "
				+ "       p.plate_level_id, "
				+ "       p.plate_name_id, "
				+ "       p.rarity_id, "
				+ "       p.stat_id, "
				+ "       p.stat_value, "
				+ "       p.stat_percent, "
				+ "       it.type_name AS item_type_name, "
				+ "       pl.level AS level, "
				+ "       r.rarity_name, "
				+ "       r.color, "
				+ "       pn.name AS plate_name, "
				+ "       pn.icon_blob, "
				+ "       pn.icon_mime, "
				+ "       pn.icon_name, "
				+ "       s.display_name AS stat_display_name "
				+ "FROM m_plate p "
				+ "LEFT JOIN m_item_types it ON p.type_id = it.type_id "
				+ "LEFT JOIN m_patch_level pl ON p.plate_level_id = pl.id "
				+ "LEFT JOIN m_rarities r ON p.rarity_id = r.rarity_id "
				+ "LEFT JOIN m_plate_name pn ON p.plate_name_id = pn.id "
				+ "LEFT JOIN m_stats s ON p.stat_id = s.stat_id "
				+ "ORDER BY pl.level, p.rarity_id, pn.name  ASC";

		return jdbcTemplate.query(sql, plateRowMapper());
	}

	@Override
	public Optional<Plate> findById(Long id) {
		String sql = ""
				+ "SELECT p.id, "
				+ "       p.type_id, "
				+ "       p.plate_level_id, "
				+ "       p.plate_name_id, "
				+ "       p.rarity_id, "
				+ "       p.stat_id, "
				+ "       p.stat_value, "
				+ "       p.stat_percent, "
				+ "       it.type_name AS item_type_name, "
				+ "       pl.level AS level, "
				+ "       r.rarity_name, "
				+ "       r.color, "
				+ "       pn.name AS plate_name, "
				+ "       pn.icon_blob, "
				+ "       pn.icon_mime, "
				+ "       pn.icon_name, "
				+ "       s.display_name AS stat_display_name "
				+ "FROM m_plate p "
				+ "LEFT JOIN m_item_types it ON p.type_id = it.type_id "
				+ "LEFT JOIN m_patch_level pl ON p.plate_level_id = pl.id "
				+ "LEFT JOIN m_rarities r ON p.rarity_id = r.rarity_id "
				+ "LEFT JOIN m_plate_name pn ON p.plate_name_id = pn.id "
				+ "LEFT JOIN m_stats s ON p.stat_id = s.stat_id "
				+ "WHERE p.id = ?";

		List<Plate> list = jdbcTemplate.query(sql, plateRowMapper(), id);
		return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
	}

	@Override
	public Optional<Plate> findIconById(Long id) {
		String sql = ""
				+ "SELECT p.id, pn.icon_blob, pn.icon_mime, pn.icon_name "
				+ "FROM m_plate p "
				+ "LEFT JOIN m_plate_name pn ON p.plate_name_id = pn.id "
				+ "WHERE p.id = ?";

		List<Plate> list = jdbcTemplate.query(sql, (rs, rowNum) -> {
			Plate p = new Plate();
			p.setId(rs.getLong("id"));
			p.setIconBlob(rs.getBytes("icon_blob"));
			p.setIconMime(rs.getString("icon_mime"));
			p.setIconName(rs.getString("icon_name"));
			return p;
		}, id);

		return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
	}

	@Override
	public boolean existsDuplicate(Integer typeId, Long plateLevelId, Long plateNameId, Integer rarityId,
			Long excludeId) {

		String sql = ""
				+ "SELECT COUNT(1) "
				+ "FROM m_plate "
				+ "WHERE type_id = ? "
				+ "  AND plate_level_id = ? "
				+ "  AND plate_name_id = ? "
				+ "  AND rarity_id = ? ";

		Integer count;

		if (excludeId != null) {
			sql += " AND id <> ? ";
			count = jdbcTemplate.queryForObject(sql, Integer.class, typeId, plateLevelId, plateNameId, rarityId,
					excludeId);
		} else {
			count = jdbcTemplate.queryForObject(sql, Integer.class, typeId, plateLevelId, plateNameId, rarityId);
		}

		return count != null && count.intValue() > 0;
	}

	@Override
	public Long insert(Integer typeId, Long plateLevelId, Long plateNameId, Integer rarityId, Integer statId,
			Integer statValue, Double statPercent) {

		String sql = ""
				+ "INSERT INTO m_plate "
				+ " (type_id, plate_level_id, plate_name_id, rarity_id, stat_id, stat_value, stat_percent) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?)";

		KeyHolder keyHolder = new GeneratedKeyHolder();

		jdbcTemplate.update(con -> {
			PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			ps.setInt(1, typeId);
			ps.setLong(2, plateLevelId);
			ps.setLong(3, plateNameId);
			ps.setInt(4, rarityId);

			if (statId != null) {
				ps.setInt(5, statId);
			} else {
				ps.setNull(5, Types.INTEGER);
			}

			if (statValue != null) {
				ps.setInt(6, statValue);
			} else {
				ps.setNull(6, Types.INTEGER);
			}

			if (statPercent != null) {
				ps.setDouble(7, statPercent);
			} else {
				ps.setNull(7, Types.REAL);
			}

			return ps;
		}, keyHolder);

		return keyHolder.getKey() == null ? null : keyHolder.getKey().longValue();
	}

	@Override
	public int update(Long id, Integer typeId, Long plateLevelId, Long plateNameId, Integer rarityId, Integer statId,
			Integer statValue, Double statPercent) {

		String sql = ""
				+ "UPDATE m_plate "
				+ "SET type_id = ?, "
				+ "    plate_level_id = ?, "
				+ "    plate_name_id = ?, "
				+ "    rarity_id = ?, "
				+ "    stat_id = ?, "
				+ "    stat_value = ?, "
				+ "    stat_percent = ? "
				+ "WHERE id = ?";

		return jdbcTemplate.update(con -> {
			PreparedStatement ps = con.prepareStatement(sql);

			ps.setInt(1, typeId);
			ps.setLong(2, plateLevelId);
			ps.setLong(3, plateNameId);
			ps.setInt(4, rarityId);

			if (statId != null) {
				ps.setInt(5, statId);
			} else {
				ps.setNull(5, Types.INTEGER);
			}

			if (statValue != null) {
				ps.setInt(6, statValue);
			} else {
				ps.setNull(6, Types.INTEGER);
			}

			if (statPercent != null) {
				ps.setDouble(7, statPercent);
			} else {
				ps.setNull(7, Types.REAL);
			}

			ps.setLong(8, id);

			return ps;
		});
	}

	@Override
	public int delete(Long id) {
		String sql = "DELETE FROM m_plate WHERE id = ?";
		return jdbcTemplate.update(sql, id);
	}

	private RowMapper<Plate> plateRowMapper() {
		return (rs, rowNum) -> {
			Plate p = new Plate();

			p.setId(rs.getLong("id"));

			Object typeIdObj = rs.getObject("type_id");
			if (typeIdObj != null) {
				p.setTypeId(rs.getInt("type_id"));
			}

			Object plateLevelIdObj = rs.getObject("plate_level_id");
			if (plateLevelIdObj != null) {
				p.setPlateLevelId(rs.getLong("plate_level_id"));
			}

			Object plateNameIdObj = rs.getObject("plate_name_id");
			if (plateNameIdObj != null) {
				p.setPlateNameId(rs.getLong("plate_name_id"));
			}

			Object rarityIdObj = rs.getObject("rarity_id");
			if (rarityIdObj != null) {
				p.setRarityId(rs.getInt("rarity_id"));
			}

			p.setPlateName(rs.getString("plate_name"));
			p.setItemTypeName(rs.getString("item_type_name"));
			p.setLevel((Integer) rs.getObject("level"));
			p.setRarityName(rs.getString("rarity_name"));
			p.setColor(rs.getString("color"));

			Object statIdObj = rs.getObject("stat_id");
			if (statIdObj != null) {
				p.setStatId(rs.getInt("stat_id"));
			}

			Object statValueObj = rs.getObject("stat_value");
			if (statValueObj != null) {
				p.setStatValue(rs.getInt("stat_value"));
			}

			Object statPercentObj = rs.getObject("stat_percent");
			if (statPercentObj != null) {
				p.setStatPercent(rs.getDouble("stat_percent"));
			}

			p.setStatDisplayName(rs.getString("stat_display_name"));

			try {
				p.setIconBlob(rs.getBytes("icon_blob"));
			} catch (Exception e) {
			}

			try {
				p.setIconMime(rs.getString("icon_mime"));
			} catch (Exception e) {
			}

			try {
				p.setIconName(rs.getString("icon_name"));
			} catch (Exception e) {
			}

			return p;
		};
	}
}