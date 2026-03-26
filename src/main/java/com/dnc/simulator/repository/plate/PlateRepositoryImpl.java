package com.dnc.simulator.repository.plate;

import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.jdbc.core.JdbcTemplate;
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

		String checkSql = "SELECT COUNT(1) FROM sqlite_master WHERE type='table' AND name=?";
		Integer cnt = jdbcTemplate.queryForObject(checkSql, Integer.class, "m_plate");
		boolean exists = cnt != null && cnt > 0;

		if (!exists) {

			String createSql = "" + "CREATE TABLE m_plate (" + "  id INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "  plate_type_id INTEGER NOT NULL," + "  plate_level_id INTEGER NOT NULL,"
					+ "  rarity_id INTEGER NOT NULL," + "  plate_name TEXT NOT NULL," + "  icon_blob BLOB,"
					+ "  icon_mime TEXT," + "  icon_name TEXT,"
					+ "  FOREIGN KEY(plate_type_id) REFERENCES m_plate_type(id),"
					+ "  FOREIGN KEY(plate_level_id) REFERENCES m_plate_level(id),"
					+ "  FOREIGN KEY(rarity_id) REFERENCES m_rarities(rarity_id)" + ");";

			jdbcTemplate.execute(createSql);

			jdbcTemplate.execute("CREATE INDEX idx_m_plate_type_id ON m_plate(plate_type_id);");
			jdbcTemplate.execute("CREATE INDEX idx_m_plate_level_id ON m_plate(plate_level_id);");
			jdbcTemplate.execute("CREATE INDEX idx_m_plate_rarity_id ON m_plate(rarity_id);");
		} else {
			// ถ้าตารางมีอยู่แล้ว แต่ยังไม่มีคอลัมน์ icon ให้เพิ่ม (SQLite รองรับ ADD
			// COLUMN)
			ensureColumn("m_plate", "icon_blob", "ALTER TABLE m_plate ADD COLUMN icon_blob BLOB");
			ensureColumn("m_plate", "icon_mime", "ALTER TABLE m_plate ADD COLUMN icon_mime TEXT");
			ensureColumn("m_plate", "icon_name", "ALTER TABLE m_plate ADD COLUMN icon_name TEXT");
		}
	}

	private void ensureColumn(String table, String column, String alterSql) {
		String pragma = "PRAGMA table_info(" + table + ")";
		List<String> cols = jdbcTemplate.query(pragma, (rs, i) -> rs.getString("name"));
		boolean has = cols.stream().anyMatch(c -> c != null && c.equalsIgnoreCase(column));
		if (!has) {
			jdbcTemplate.execute(alterSql);
		}
	}

	@Override
	public List<Plate> findAll() {

		String sql = "" + "SELECT " + "  p.id, p.plate_type_id, p.plate_level_id, p.rarity_id, p.plate_name, "
				+ "  t.name AS type_name, " + "  l.level AS level_value, " + "  r.rarity_name, r.color, "
				+ "  CASE WHEN p.icon_blob IS NULL THEN 0 ELSE 1 END AS has_icon " + "FROM m_plate p "
				+ "LEFT JOIN m_plate_type t ON t.id = p.plate_type_id "
				+ "LEFT JOIN m_plate_level l ON l.id = p.plate_level_id "
				+ "LEFT JOIN m_rarities r ON r.rarity_id = p.rarity_id " + "ORDER BY p.id";

		return jdbcTemplate.query(sql, (rs, i) -> {
			Plate x = new Plate();
			x.setId(rs.getLong("id"));
			x.setPlateTypeId(rs.getLong("plate_type_id"));
			x.setPlateLevelId(rs.getLong("plate_level_id"));
			x.setRarityId(rs.getInt("rarity_id"));
			x.setPlateName(rs.getString("plate_name"));

			x.setTypeName(rs.getString("type_name"));
			x.setLevel(rs.getInt("level_value"));
			x.setRarityName(rs.getString("rarity_name"));
			x.setColor(rs.getString("color"));

			// ไม่ดึง blob แต่ถ้าคุณอยากแสดงว่า “มีรูปไหม” ใช้ has_icon ไปทำ UI ต่อได้
			return x;
		});
	}

	@Override
	public Optional<Plate> findById(Long id) {

		String sql = "" + "SELECT " + "  p.id, p.plate_type_id, p.plate_level_id, p.rarity_id, p.plate_name, "
				+ "  t.name AS type_name, " + "  l.level AS level_value, " + "  r.rarity_name, r.color, "
				+ "  p.icon_mime, p.icon_name " + "FROM m_plate p "
				+ "LEFT JOIN m_plate_type t ON t.id = p.plate_type_id "
				+ "LEFT JOIN m_plate_level l ON l.id = p.plate_level_id "
				+ "LEFT JOIN m_rarities r ON r.rarity_id = p.rarity_id " + "WHERE p.id=?";

		List<Plate> list = jdbcTemplate.query(sql, (rs, i) -> {
			Plate x = new Plate();
			x.setId(rs.getLong("id"));
			x.setPlateTypeId(rs.getLong("plate_type_id"));
			x.setPlateLevelId(rs.getLong("plate_level_id"));
			x.setRarityId(rs.getInt("rarity_id"));
			x.setPlateName(rs.getString("plate_name"));

			x.setTypeName(rs.getString("type_name"));
			x.setLevel(rs.getInt("level_value"));
			x.setRarityName(rs.getString("rarity_name"));
			x.setColor(rs.getString("color"));

			x.setIconMime(rs.getString("icon_mime"));
			x.setIconName(rs.getString("icon_name"));
			return x;
		}, id);

		return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
	}

	@Override
	public Optional<Plate> findIconById(Long id) {

		String sql = "SELECT id, icon_blob, icon_mime, icon_name FROM m_plate WHERE id=?";

		List<Plate> list = jdbcTemplate.query(sql, (rs, i) -> {
			Plate x = new Plate();
			x.setId(rs.getLong("id"));
			x.setIconBlob(rs.getBytes("icon_blob"));
			x.setIconMime(rs.getString("icon_mime"));
			x.setIconName(rs.getString("icon_name"));
			return x;
		}, id);

		return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
	}

	@Override
	public Long insert(Long plateTypeId, Long plateLevelId, Integer rarityId, String plateName, byte[] iconBlob,
			String iconMime, String iconName) {

		String sql = ""
				+ "INSERT INTO m_plate(plate_type_id, plate_level_id, rarity_id, plate_name, icon_blob, icon_mime, icon_name) "
				+ "VALUES(?,?,?,?,?,?,?)";

		jdbcTemplate.update(sql, plateTypeId, plateLevelId, rarityId, plateName, iconBlob, iconMime, iconName);

		return jdbcTemplate.queryForObject("SELECT last_insert_rowid()", Long.class);
	}

	@Override
	public int update(Long id, Long plateTypeId, Long plateLevelId, Integer rarityId, String plateName, byte[] iconBlob,
			String iconMime, String iconName) {

		String sql = "" + "UPDATE m_plate SET " + " plate_type_id=?, plate_level_id=?, rarity_id=?, plate_name=?, "
				+ " icon_blob=?, icon_mime=?, icon_name=? " + "WHERE id=?";

		return jdbcTemplate.update(sql, plateTypeId, plateLevelId, rarityId, plateName, iconBlob, iconMime, iconName,
				id);
	}

	@Override
	public int updateNoIcon(Long id, Long plateTypeId, Long plateLevelId, Integer rarityId, String plateName) {

		String sql = "UPDATE m_plate SET plate_type_id=?, plate_level_id=?, rarity_id=?, plate_name=? WHERE id=?";
		return jdbcTemplate.update(sql, plateTypeId, plateLevelId, rarityId, plateName, id);
	}

	@Override
	public int delete(Long id) {
		String sql = "DELETE FROM m_plate WHERE id=?";
		return jdbcTemplate.update(sql, id);
	}
}