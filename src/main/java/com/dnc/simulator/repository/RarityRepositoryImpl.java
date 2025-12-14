package com.dnc.simulator.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.dnc.simulator.model.Rarity;
import com.dnc.simulator.repository.RarityRepository;

@Repository
public class RarityRepositoryImpl implements RarityRepository {

	private final JdbcTemplate jdbcTemplate;

	public RarityRepositoryImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private static final RowMapper<Rarity> ROW_MAPPER = new RowMapper<Rarity>() {
		@Override
		public Rarity mapRow(ResultSet rs, int rowNum) throws SQLException {
			Rarity r = new Rarity();
			r.setRarityId(rs.getInt("rarity_id"));
			r.setRarityName(rs.getString("rarity_name"));
			r.setColor(rs.getString("color"));
			return r;
		}
	};

	@Override
	public List<Rarity> findAll() {
		return jdbcTemplate.query("SELECT * FROM m_rarities ORDER BY rarity_id", ROW_MAPPER);
	}

	@Override
	public Rarity findById(int id) {
		List<Rarity> list = jdbcTemplate.query("SELECT * FROM m_rarities WHERE rarity_id = ?", ROW_MAPPER, id);
		return list.isEmpty() ? null : list.get(0);
	}

	@Override
	public void insert(Rarity rarity) {
		jdbcTemplate.update("INSERT INTO m_rarities (rarity_id, rarity_name, color) VALUES (?, ?, ?)",
				rarity.getRarityId(), rarity.getRarityName(), rarity.getColor());
	}

	@Override
	public void update(Rarity rarity) {
		jdbcTemplate.update("UPDATE m_rarities SET rarity_name = ?, color = ? WHERE rarity_id = ?",
				rarity.getRarityName(), rarity.getColor(), rarity.getRarityId());
	}

	@Override
	public void delete(int id) {
		jdbcTemplate.update("DELETE FROM m_rarities WHERE rarity_id = ?", id);
	}
}
