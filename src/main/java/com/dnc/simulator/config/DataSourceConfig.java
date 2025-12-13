package com.dnc.simulator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.sqlite.SQLiteDataSource;

import javax.sql.DataSource;
import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Properties;

@Configuration
public class DataSourceConfig {

	private static final String PROP_FILE = "dnc-dev.properties";
	private static final String DB_PATH_KEY = "db.path";

	@Bean
	public DataSource dataSource() {

		try {
			// ===============================
			// 1. Load properties from classpath
			// ===============================
			Properties p = new Properties();
			try (InputStream is = getClass().getClassLoader().getResourceAsStream(PROP_FILE)) {

				if (is == null) {
					throw new RuntimeException("Cannot find " + PROP_FILE + " in classpath (src/main/resources)");
				}
				p.load(is);
			}

			String dbPath = p.getProperty(DB_PATH_KEY);
			if (dbPath == null || dbPath.trim().isEmpty()) {
				throw new RuntimeException("Missing '" + DB_PATH_KEY + "' in " + PROP_FILE);
			}

			// ===============================
			// 2. Resolve DB file (filesystem)
			// ===============================
			File dbFile = new File(dbPath);

			if (!dbFile.getParentFile().exists()) {
				dbFile.getParentFile().mkdirs();
			}

			String jdbcUrl = "jdbc:sqlite:" + dbFile.getAbsolutePath();
			System.out.println("[SQLite] PROPERTIES = classpath:" + PROP_FILE);
			System.out.println("[SQLite] DB         = " + jdbcUrl);

			// ===============================
			// 3. Create DataSource
			// ===============================
			SQLiteDataSource ds = new SQLiteDataSource();
			ds.setUrl(jdbcUrl);

			// ===============================
			// 4. Init SQLite PRAGMA
			// ===============================
			try (Connection conn = ds.getConnection(); Statement stmt = conn.createStatement()) {

				stmt.execute("PRAGMA foreign_keys = ON");
				stmt.execute("PRAGMA journal_mode = WAL");
				stmt.execute("PRAGMA busy_timeout = 5000");
			}

			return ds;

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to init SQLite DB", e);
		}
	}

	@Bean
	public JdbcTemplate jdbcTemplate(DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}
}
