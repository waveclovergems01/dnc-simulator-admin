package com.dnc.simulator.debug;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.io.File;

public class SqliteDirectInsertTest {

	// ✅ DB อยู่ใน project (relative path)
	private static final String RELATIVE_DB_PATH = "db/dnc-simulator.db";

	public static void main(String[] args) {

		// แปลงเป็น absolute path ตามที่ project ถูก clone
		File dbFile = new File(RELATIVE_DB_PATH);
		String absolutePath = dbFile.getAbsolutePath();
		String jdbcUrl = "jdbc:sqlite:" + absolutePath;

		System.out.println("Working Dir  = " + System.getProperty("user.dir"));
		System.out.println("SQLite DB   = " + absolutePath);
		System.out.println("JDBC URL   = " + jdbcUrl);

		Connection conn = null;

		try {
			// ===== 0. สร้างโฟลเดอร์ db ถ้ายังไม่มี =====
//			if (!dbFile.getParentFile().exists()) {
//				dbFile.getParentFile().mkdirs();
//			}

			// ===== 1. Connect =====
			conn = DriverManager.getConnection(jdbcUrl);
			conn.setAutoCommit(false);

			// ===== 2. CREATE TABLE =====
//			String createTableSql = "CREATE TABLE IF NOT EXISTS jobs (" + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
//					+ " name TEXT NOT NULL," + " class TEXT NOT NULL," + " inherit_id INTEGER,"
//					+ " required_level INTEGER" + ")";
//
//			Statement stmt = conn.createStatement();
//			stmt.execute(createTableSql);
//			stmt.close();

			// ===== 3. INSERT =====
			String insertSql = "INSERT INTO jobs (name, class, inherit_id, required_level) " + "VALUES (?, ?, ?, ?)";

			PreparedStatement ps = conn.prepareStatement(insertSql);
			ps.setString(1, "Warrior");
			ps.setString(2, "Base");
			ps.setInt(3, 0);
			ps.setInt(4, 1);
			ps.executeUpdate();
			ps.close();

			// ===== 4. COMMIT =====
			conn.commit();

			System.out.println("✅ INSERT SUCCESS (relative project DB)");

		} catch (Exception e) {
			e.printStackTrace();
			try {
				if (conn != null) {
					conn.rollback();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
