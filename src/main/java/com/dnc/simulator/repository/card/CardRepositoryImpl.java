package com.dnc.simulator.repository.card;

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
import org.springframework.transaction.annotation.Transactional;

import com.dnc.simulator.model.card.Card;
import com.dnc.simulator.model.card.CardStat;

@Repository
public class CardRepositoryImpl implements CardRepository {

	private final JdbcTemplate jdbcTemplate;

	public CardRepositoryImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@PostConstruct
	public void init() {
		ensureTable();
	}

	@Override
	public void ensureTable() {
		createCardTableIfNotExists();
		createCardStatTableIfNotExists();
		ensureUniqueIndex();
	}

	private void createCardTableIfNotExists() {
		String sql = "" + "CREATE TABLE IF NOT EXISTS m_card (" + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ " type_id INTEGER NOT NULL," + " card_level_id INTEGER NOT NULL," + " card_name_id INTEGER NOT NULL,"
				+ " rarity_id INTEGER NOT NULL," + " slot_number INTEGER NOT NULL DEFAULT 0" + ")";
		jdbcTemplate.execute(sql);

		ensureCardColumns();
	}

	private void ensureCardColumns() {
		if (!columnExists("m_card", "slot_number")) {
			jdbcTemplate.execute("ALTER TABLE m_card ADD COLUMN slot_number INTEGER NOT NULL DEFAULT 0");
		}
	}

	private void createCardStatTableIfNotExists() {
		String sql = "" + "CREATE TABLE IF NOT EXISTS m_card_stat (" + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ " card_id INTEGER NOT NULL," + " stat_id INTEGER NOT NULL," + " value_min REAL NOT NULL,"
				+ " value_max REAL NOT NULL,"
				+ " is_percentage INTEGER NOT NULL DEFAULT 0 CHECK(is_percentage IN (0, 1))" + ")";
		jdbcTemplate.execute(sql);

		migrateCardStatTableIfNeeded();
	}

	private void migrateCardStatTableIfNeeded() {
		boolean hasValue = columnExists("m_card_stat", "value");
		boolean hasValueMin = columnExists("m_card_stat", "value_min");
		boolean hasValueMax = columnExists("m_card_stat", "value_max");
		boolean hasIsPercentage = columnExists("m_card_stat", "is_percentage");

		if (!hasValue && hasValueMin && hasValueMax && hasIsPercentage) {
			return;
		}

		jdbcTemplate.execute("DROP TABLE IF EXISTS m_card_stat_new");

		String createSql = "" + "CREATE TABLE m_card_stat_new (" + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ " card_id INTEGER NOT NULL," + " stat_id INTEGER NOT NULL," + " value_min REAL NOT NULL,"
				+ " value_max REAL NOT NULL,"
				+ " is_percentage INTEGER NOT NULL DEFAULT 0 CHECK(is_percentage IN (0, 1))" + ")";
		jdbcTemplate.execute(createSql);

		if (hasValue) {
			jdbcTemplate.execute("" + "INSERT INTO m_card_stat_new "
					+ " (id, card_id, stat_id, value_min, value_max, is_percentage) "
					+ "SELECT id, card_id, stat_id, value, value, 0 " + "FROM m_card_stat");
		} else if (hasValueMin && hasValueMax) {
			String isPercentageSelect = hasIsPercentage ? "is_percentage" : "0";

			jdbcTemplate.execute("" + "INSERT INTO m_card_stat_new "
					+ " (id, card_id, stat_id, value_min, value_max, is_percentage) "
					+ "SELECT id, card_id, stat_id, value_min, value_max, " + isPercentageSelect + " "
					+ "FROM m_card_stat");
		}

		jdbcTemplate.execute("DROP TABLE m_card_stat");
		jdbcTemplate.execute("ALTER TABLE m_card_stat_new RENAME TO m_card_stat");
	}

	private void ensureUniqueIndex() {
		try {
			jdbcTemplate.execute("DROP INDEX IF EXISTS uk_m_card_unique_key");
		} catch (Exception e) {
		}

		try {
			jdbcTemplate.execute("" + "CREATE UNIQUE INDEX IF NOT EXISTS uk_m_card_unique_key "
					+ "ON m_card (type_id, card_level_id, card_name_id, rarity_id)");
		} catch (Exception e) {
			throw new RuntimeException("Cannot create unique index for m_card. Please remove duplicate data first.", e);
		}

		try {
			jdbcTemplate.execute("" + "CREATE UNIQUE INDEX IF NOT EXISTS uk_m_card_stat_unique_key "
					+ "ON m_card_stat (card_id, stat_id)");
		} catch (Exception e) {
			throw new RuntimeException(
					"Cannot create unique index for m_card_stat. Please remove duplicate stat first.", e);
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
	public List<Card> findAll() {
		String sql = "" + "SELECT c.id, " + "       c.type_id, " + "       c.card_level_id, "
				+ "       c.card_name_id, " + "       c.rarity_id, " + "       c.slot_number, "
				+ "       it.type_name AS item_type_name, " + "       pl.level AS level, " + "       r.rarity_name, "
				+ "       r.color, " + "       cn.name AS card_name, " + "       cn.icon_blob, "
				+ "       cn.icon_mime, " + "       cn.icon_name " + "FROM m_card c "
				+ "LEFT JOIN m_item_types it ON c.type_id = it.type_id "
				+ "LEFT JOIN m_patch_level pl ON c.card_level_id = pl.id "
				+ "LEFT JOIN m_rarities r ON c.rarity_id = r.rarity_id "
				+ "LEFT JOIN m_card_name cn ON c.card_name_id = cn.id "
				+ "ORDER BY c.slot_number, pl.level, cn.name, c.rarity_id ASC";

		List<Card> list = jdbcTemplate.query(sql, cardRowMapper());

		for (Card card : list) {
			card.setStatList(findStatsByCardId(card.getId()));
		}

		return list;
	}

	@Override
	public Optional<Card> findById(Long id) {
		String sql = "" + "SELECT c.id, " + "       c.type_id, " + "       c.card_level_id, "
				+ "       c.card_name_id, " + "       c.rarity_id, " + "       c.slot_number, "
				+ "       it.type_name AS item_type_name, " + "       pl.level AS level, " + "       r.rarity_name, "
				+ "       r.color, " + "       cn.name AS card_name, " + "       cn.icon_blob, "
				+ "       cn.icon_mime, " + "       cn.icon_name " + "FROM m_card c "
				+ "LEFT JOIN m_item_types it ON c.type_id = it.type_id "
				+ "LEFT JOIN m_patch_level pl ON c.card_level_id = pl.id "
				+ "LEFT JOIN m_rarities r ON c.rarity_id = r.rarity_id "
				+ "LEFT JOIN m_card_name cn ON c.card_name_id = cn.id " + "WHERE c.id = ?";

		List<Card> list = jdbcTemplate.query(sql, cardRowMapper(), id);

		if (list.isEmpty()) {
			return Optional.empty();
		}

		Card card = list.get(0);
		card.setStatList(findStatsByCardId(card.getId()));

		return Optional.of(card);
	}

	@Override
	public Optional<Card> findIconById(Long id) {
		String sql = "" + "SELECT c.id, cn.icon_blob, cn.icon_mime, cn.icon_name " + "FROM m_card c "
				+ "LEFT JOIN m_card_name cn ON c.card_name_id = cn.id " + "WHERE c.id = ?";

		List<Card> list = jdbcTemplate.query(sql, (rs, rowNum) -> {
			Card c = new Card();
			c.setId(rs.getLong("id"));
			c.setIconBlob(rs.getBytes("icon_blob"));
			c.setIconMime(rs.getString("icon_mime"));
			c.setIconName(rs.getString("icon_name"));
			return c;
		}, id);

		return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
	}

	@Override
	public List<CardStat> findStatsByCardId(Long cardId) {
		String sql = "" + "SELECT cs.id, " + "       cs.card_id, " + "       cs.stat_id, " + "       cs.value_min, "
				+ "       cs.value_max, " + "       cs.is_percentage, " + "       s.stat_name, "
				+ "       s.display_name AS stat_display_name " + "FROM m_card_stat cs "
				+ "LEFT JOIN m_stats s ON cs.stat_id = s.stat_id " + "WHERE cs.card_id = ? " + "ORDER BY cs.id ASC";

		return jdbcTemplate.query(sql, cardStatRowMapper(), cardId);
	}

	@Override
	public boolean existsDuplicate(Integer typeId, Long cardLevelId, Long cardNameId, Integer rarityId,
			Long excludeId) {
		String sql = "" + "SELECT COUNT(1) " + "FROM m_card " + "WHERE type_id = ? " + "  AND card_level_id = ? "
				+ "  AND card_name_id = ? " + "  AND rarity_id = ? ";

		Integer count;

		if (excludeId != null) {
			sql += " AND id <> ? ";
			count = jdbcTemplate.queryForObject(sql, Integer.class, typeId, cardLevelId, cardNameId, rarityId,
					excludeId);
		} else {
			count = jdbcTemplate.queryForObject(sql, Integer.class, typeId, cardLevelId, cardNameId, rarityId);
		}

		return count != null && count.intValue() > 0;
	}

	@Override
	public Long insert(Integer typeId, Long cardLevelId, Long cardNameId, Integer rarityId, Integer slotNumber) {
		String sql = "" + "INSERT INTO m_card " + " (type_id, card_level_id, card_name_id, rarity_id, slot_number) "
				+ "VALUES (?, ?, ?, ?, ?)";

		KeyHolder keyHolder = new GeneratedKeyHolder();

		jdbcTemplate.update(con -> {
			PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, typeId);
			ps.setLong(2, cardLevelId);
			ps.setLong(3, cardNameId);
			ps.setInt(4, rarityId);
			ps.setInt(5, slotNumber == null ? 0 : slotNumber);
			return ps;
		}, keyHolder);

		return keyHolder.getKey() == null ? null : keyHolder.getKey().longValue();
	}

	@Override
	public int update(Long id, Integer typeId, Long cardLevelId, Long cardNameId, Integer rarityId,
			Integer slotNumber) {
		String sql = "" + "UPDATE m_card " + "SET type_id = ?, " + "    card_level_id = ?, " + "    card_name_id = ?, "
				+ "    rarity_id = ?, " + "    slot_number = ? " + "WHERE id = ?";

		return jdbcTemplate.update(sql, typeId, cardLevelId, cardNameId, rarityId, slotNumber, id);
	}

	@Override
	@Transactional
	public int delete(Long id) {
		deleteStatsByCardId(id);

		String sql = "DELETE FROM m_card WHERE id = ?";
		return jdbcTemplate.update(sql, id);
	}

	@Override
	public void deleteStatsByCardId(Long cardId) {
		String sql = "DELETE FROM m_card_stat WHERE card_id = ?";
		jdbcTemplate.update(sql, cardId);
	}

	@Override
	public void insertStat(Long cardId, Integer statId, Double valueMin, Double valueMax, Integer isPercentage) {
		String sql = "" + "INSERT INTO m_card_stat " + " (card_id, stat_id, value_min, value_max, is_percentage) "
				+ "VALUES (?, ?, ?, ?, ?)";

		jdbcTemplate.update(con -> {
			PreparedStatement ps = con.prepareStatement(sql);

			ps.setLong(1, cardId);
			ps.setInt(2, statId);
			ps.setDouble(3, valueMin);
			ps.setDouble(4, valueMax);
			ps.setInt(5, isPercentage == null ? 0 : isPercentage);

			return ps;
		});
	}

	@Override
	@Transactional
	public void replaceStats(Long cardId, List<CardStat> statList) {
		deleteStatsByCardId(cardId);

		if (statList == null || statList.isEmpty()) {
			return;
		}

		for (CardStat stat : statList) {
			insertStat(cardId, stat.getStatId(), stat.getValueMin(), stat.getValueMax(), stat.getIsPercentage());
		}
	}

	private RowMapper<Card> cardRowMapper() {
		return (rs, rowNum) -> {
			Card c = new Card();

			c.setId(rs.getLong("id"));

			Object typeIdObj = rs.getObject("type_id");
			if (typeIdObj != null) {
				c.setTypeId(rs.getInt("type_id"));
			}

			Object cardLevelIdObj = rs.getObject("card_level_id");
			if (cardLevelIdObj != null) {
				c.setCardLevelId(rs.getLong("card_level_id"));
			}

			Object cardNameIdObj = rs.getObject("card_name_id");
			if (cardNameIdObj != null) {
				c.setCardNameId(rs.getLong("card_name_id"));
			}

			Object rarityIdObj = rs.getObject("rarity_id");
			if (rarityIdObj != null) {
				c.setRarityId(rs.getInt("rarity_id"));
			}

			Object slotNumberObj = rs.getObject("slot_number");
			if (slotNumberObj != null) {
				c.setSlotNumber(rs.getInt("slot_number"));
			}

			c.setItemTypeName(rs.getString("item_type_name"));

			Object levelObj = rs.getObject("level");
			if (levelObj != null) {
				c.setLevel(rs.getInt("level"));
			}

			c.setRarityName(rs.getString("rarity_name"));
			c.setColor(rs.getString("color"));
			c.setCardName(rs.getString("card_name"));

			try {
				c.setIconBlob(rs.getBytes("icon_blob"));
			} catch (Exception e) {
			}

			try {
				c.setIconMime(rs.getString("icon_mime"));
			} catch (Exception e) {
			}

			try {
				c.setIconName(rs.getString("icon_name"));
			} catch (Exception e) {
			}

			return c;
		};
	}

	private RowMapper<CardStat> cardStatRowMapper() {
		return (rs, rowNum) -> {
			CardStat s = new CardStat();

			s.setId(rs.getLong("id"));
			s.setCardId(rs.getLong("card_id"));

			Object statIdObj = rs.getObject("stat_id");
			if (statIdObj != null) {
				s.setStatId(rs.getInt("stat_id"));
			}

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
			}

			s.setStatName(rs.getString("stat_name"));
			s.setStatDisplayName(rs.getString("stat_display_name"));

			return s;
		};
	}
}