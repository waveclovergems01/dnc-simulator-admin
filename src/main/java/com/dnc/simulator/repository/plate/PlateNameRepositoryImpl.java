package com.dnc.simulator.repository.plate;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.dnc.simulator.model.plate.PlateName;

@Repository
public class PlateNameRepositoryImpl implements PlateNameRepository {

	private final JdbcTemplate jdbcTemplate;

	public PlateNameRepositoryImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@PostConstruct
	public void init() {
		createTableIfNotExists();
		ensureIconColumns();
	}

	private void createTableIfNotExists() {
		String sql = "" + "CREATE TABLE IF NOT EXISTS m_plate_name (" + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ " name TEXT NOT NULL," + " icon_blob BLOB," + " icon_mime TEXT," + " icon_name TEXT" + ")";
		jdbcTemplate.execute(sql);
	}

	private void ensureIconColumns() {
		try {
			jdbcTemplate.execute("ALTER TABLE m_plate_name ADD COLUMN icon_blob BLOB");
		} catch (Exception e) {
		}

		try {
			jdbcTemplate.execute("ALTER TABLE m_plate_name ADD COLUMN icon_mime TEXT");
		} catch (Exception e) {
		}

		try {
			jdbcTemplate.execute("ALTER TABLE m_plate_name ADD COLUMN icon_name TEXT");
		} catch (Exception e) {
		}
	}

	@Override
	public List<PlateName> findAll() {
		String sql = "SELECT id, name, icon_blob, icon_mime, icon_name FROM m_plate_name ORDER BY id ASC";
		return jdbcTemplate.query(sql, (rs, rowNum) -> {
			PlateName x = new PlateName();
			x.setId(rs.getLong("id"));
			x.setName(rs.getString("name"));
			x.setIconBlob(rs.getBytes("icon_blob"));
			x.setIconMime(rs.getString("icon_mime"));
			x.setIconName(rs.getString("icon_name"));
			return x;
		});
	}

	@Override
	public Optional<PlateName> findById(Long id) {
		String sql = "SELECT id, name, icon_blob, icon_mime, icon_name FROM m_plate_name WHERE id = ?";
		List<PlateName> list = jdbcTemplate.query(sql, new Object[] { id }, (rs, rowNum) -> {
			PlateName x = new PlateName();
			x.setId(rs.getLong("id"));
			x.setName(rs.getString("name"));
			x.setIconBlob(rs.getBytes("icon_blob"));
			x.setIconMime(rs.getString("icon_mime"));
			x.setIconName(rs.getString("icon_name"));
			return x;
		});
		return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
	}

	@Override
	public Optional<PlateName> findIconById(Long id) {
		String sql = "SELECT id, icon_blob, icon_mime, icon_name FROM m_plate_name WHERE id = ?";
		List<PlateName> list = jdbcTemplate.query(sql, new Object[] { id }, (rs, rowNum) -> {
			PlateName x = new PlateName();
			x.setId(rs.getLong("id"));
			x.setIconBlob(rs.getBytes("icon_blob"));
			x.setIconMime(rs.getString("icon_mime"));
			x.setIconName(rs.getString("icon_name"));
			return x;
		});
		return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
	}

	@Override
	public Long insert(String name, byte[] iconBlob, String iconMime, String iconName) {
		String sql = "INSERT INTO m_plate_name (name, icon_blob, icon_mime, icon_name) VALUES (?, ?, ?, ?)";

		KeyHolder keyHolder = new GeneratedKeyHolder();

		jdbcTemplate.update(con -> {
			PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, name);
			ps.setBytes(2, iconBlob);
			ps.setString(3, iconMime);
			ps.setString(4, iconName);
			return ps;
		}, keyHolder);

		return keyHolder.getKey() == null ? null : keyHolder.getKey().longValue();
	}

	@Override
	public int update(Long id, String name, byte[] iconBlob, String iconMime, String iconName) {
		String sql = "UPDATE m_plate_name SET name = ?, icon_blob = ?, icon_mime = ?, icon_name = ? WHERE id = ?";
		return jdbcTemplate.update(sql, name, iconBlob, iconMime, iconName, id);
	}

	@Override
	public int updateNoIcon(Long id, String name) {
		String sql = "UPDATE m_plate_name SET name = ? WHERE id = ?";
		return jdbcTemplate.update(sql, name, id);
	}

	@Override
	public int delete(Long id) {
		String sql = "DELETE FROM m_plate_name WHERE id = ?";
		return jdbcTemplate.update(sql, id);
	}
}