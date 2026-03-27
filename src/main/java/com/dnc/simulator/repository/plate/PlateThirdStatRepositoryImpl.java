package com.dnc.simulator.repository.plate;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.dnc.simulator.model.plate.PlateThirdStat;

@Repository
public class PlateThirdStatRepositoryImpl implements PlateThirdStatRepository {

	private final JdbcTemplate jdbcTemplate;

	public PlateThirdStatRepositoryImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@PostConstruct
	public void init() {
		ensureTable();
	}

	@Override
	public void ensureTable() {
		String sql = "" + "CREATE TABLE IF NOT EXISTS m_plate_3rd_stat (" + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ " stat_id INTEGER NOT NULL," + " rarity_id INTEGER NOT NULL," + " patch_level_id INTEGER NOT NULL,"
				+ " value INTEGER NOT NULL" + ")";
		jdbcTemplate.execute(sql);
	}

	@Override
	public List<PlateThirdStat> findAll() {
		String sql = "" + "SELECT pts.id, pts.stat_id, pts.rarity_id, pts.patch_level_id, pts.value, "
				+ "       s.display_name AS stat_display_name, " + "       r.rarity_name, r.color, "
				+ "       pl.level AS level " + "FROM m_plate_3rd_stat pts "
				+ "LEFT JOIN m_stats s ON pts.stat_id = s.stat_id "
				+ "LEFT JOIN m_rarities r ON pts.rarity_id = r.rarity_id "
				+ "LEFT JOIN m_patch_level pl ON pts.patch_level_id = pl.id " + "ORDER BY pts.id ASC";

		return jdbcTemplate.query(sql, rowMapper());
	}

	@Override
	public Optional<PlateThirdStat> findById(Long id) {
		String sql = "" + "SELECT pts.id, pts.stat_id, pts.rarity_id, pts.patch_level_id, pts.value, "
				+ "       s.display_name AS stat_display_name, " + "       r.rarity_name, r.color, "
				+ "       pl.level AS level " + "FROM m_plate_3rd_stat pts "
				+ "LEFT JOIN m_stats s ON pts.stat_id = s.stat_id "
				+ "LEFT JOIN m_rarities r ON pts.rarity_id = r.rarity_id "
				+ "LEFT JOIN m_patch_level pl ON pts.patch_level_id = pl.id " + "WHERE pts.id = ?";

		List<PlateThirdStat> list = jdbcTemplate.query(sql, rowMapper(), id);
		return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
	}

	@Override
	public Long insert(Integer statId, Integer rarityId, Long patchLevelId, Integer value) {
		String sql = "" + "INSERT INTO m_plate_3rd_stat (stat_id, rarity_id, patch_level_id, value) "
				+ "VALUES (?, ?, ?, ?)";

		KeyHolder keyHolder = new GeneratedKeyHolder();

		jdbcTemplate.update(con -> {
			PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, statId);
			ps.setInt(2, rarityId);
			ps.setLong(3, patchLevelId);
			ps.setInt(4, value);
			return ps;
		}, keyHolder);

		return keyHolder.getKey() == null ? null : keyHolder.getKey().longValue();
	}

	@Override
	public int update(Long id, Integer statId, Integer rarityId, Long patchLevelId, Integer value) {
		String sql = "" + "UPDATE m_plate_3rd_stat " + "SET stat_id = ?, rarity_id = ?, patch_level_id = ?, value = ? "
				+ "WHERE id = ?";

		return jdbcTemplate.update(sql, statId, rarityId, patchLevelId, value, id);
	}

	@Override
	public int delete(Long id) {
		String sql = "DELETE FROM m_plate_3rd_stat WHERE id = ?";
		return jdbcTemplate.update(sql, id);
	}

	private RowMapper<PlateThirdStat> rowMapper() {
		return (rs, rowNum) -> {
			PlateThirdStat x = new PlateThirdStat();
			x.setId(rs.getLong("id"));
			x.setStatId(rs.getInt("stat_id"));
			x.setRarityId(rs.getInt("rarity_id"));
			x.setPatchLevelId(rs.getLong("patch_level_id"));
			x.setValue(rs.getInt("value"));
			x.setStatDisplayName(rs.getString("stat_display_name"));
			x.setRarityName(rs.getString("rarity_name"));
			x.setColor(rs.getString("color"));
			x.setLevel((Integer) rs.getObject("level"));
			return x;
		};
	}
}