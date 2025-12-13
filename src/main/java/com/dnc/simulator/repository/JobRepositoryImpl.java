package com.dnc.simulator.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.dnc.simulator.model.Job;

@Repository
public class JobRepositoryImpl implements JobRepository {

	private final JdbcTemplate jdbcTemplate;

	public JobRepositoryImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/*
	 * ========================= FIND ALL (job + next classes)
	 * =========================
	 */
	@Override
	public List<Job> findAll() {

		String sql = "SELECT " + " j.id AS job_id, " + " j.name AS job_name, " + " j.class_id, " + " j.class_name, "
				+ " j.inherit, " + " j.required_level, " + " nc.next_class_id " + "FROM m_jobs j "
				+ "LEFT JOIN m_job_next_classes nc " + " ON j.id = nc.job_id " + "ORDER BY j.id, nc.next_class_id";

		return jdbcTemplate.query(sql, new ResultSetExtractor<List<Job>>() {

			@Override
			public List<Job> extractData(ResultSet rs) throws SQLException {

				Map<Integer, Job> map = new LinkedHashMap<>();

				while (rs.next()) {
					int jobId = rs.getInt("job_id");

					Job job = map.get(jobId);
					if (job == null) {
						job = new Job();
						job.setId(jobId);
						job.setName(rs.getString("job_name"));
						job.setClassId(rs.getInt("class_id"));
						job.setClassName(rs.getString("class_name"));
						job.setInherit(rs.getInt("inherit"));
						job.setRequiredLevel(rs.getInt("required_level"));
						map.put(jobId, job);
					}

					int nextId = rs.getInt("next_class_id");
					if (!rs.wasNull()) {
						job.getNextClassIds().add(nextId);
					}
				}

				return new ArrayList<>(map.values());
			}
		});
	}

	/*
	 * ========================= FIND BY ID =========================
	 */
	@Override
	public Job findById(int id) {

		String sql = "SELECT id, name, class_id, class_name, inherit, required_level " + "FROM m_jobs WHERE id = ?";

		Job job = jdbcTemplate.queryForObject(sql, new Object[] { id }, new RowMapper<Job>() {
			@Override
			public Job mapRow(ResultSet rs, int rowNum) throws SQLException {
				Job j = new Job();
				j.setId(rs.getInt("id"));
				j.setName(rs.getString("name"));
				j.setClassId(rs.getInt("class_id"));
				j.setClassName(rs.getString("class_name"));
				j.setInherit(rs.getInt("inherit"));
				j.setRequiredLevel(rs.getInt("required_level"));
				return j;
			}
		});

		String sqlNext = "SELECT next_class_id FROM m_job_next_classes WHERE job_id = ?";

		List<Integer> nextIds = jdbcTemplate.query(sqlNext, new Object[] { id },
				(rs, rowNum) -> rs.getInt("next_class_id"));

		job.getNextClassIds().addAll(nextIds);
		return job;
	}

	/*
	 * ========================= INSERT (2 tables) =========================
	 */
	@Override
	public void insert(Job job) {

		String sql = "INSERT INTO m_jobs " + "(id, name, class_id, class_name, inherit, required_level) "
				+ "VALUES (?, ?, ?, ?, ?, ?)";

		jdbcTemplate.update(sql, job.getId(), job.getName(), job.getClassId(), job.getClassName(), job.getInherit(),
				job.getRequiredLevel());

		insertNextClasses(job);
	}

	/*
	 * ========================= DELETE =========================
	 */
	@Override
	public void delete(int id) {

		jdbcTemplate.update("DELETE FROM m_job_next_classes WHERE job_id = ?", id);

		jdbcTemplate.update("DELETE FROM m_jobs WHERE id = ?", id);
	}

	/*
	 * ========================= HELPER =========================
	 */
	private void insertNextClasses(Job job) {

		if (job.getNextClassIds() == null || job.getNextClassIds().isEmpty()) {
			return;
		}

		String sql = "INSERT INTO m_job_next_classes (job_id, next_class_id) VALUES (?, ?)";

		for (Integer nextId : job.getNextClassIds()) {
			jdbcTemplate.update(sql, job.getId(), nextId);
		}
	}
}
