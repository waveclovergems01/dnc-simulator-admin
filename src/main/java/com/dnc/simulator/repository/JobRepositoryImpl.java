package com.dnc.simulator.repository;

import com.dnc.simulator.model.Job;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JobRepositoryImpl implements JobRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Job> getAllJobs() {
		return jdbcTemplate.query("SELECT * FROM m_jobs ORDER BY class_id, id", (rs, i) -> {
			Job j = new Job();
			j.setId(rs.getInt("id"));
			j.setName(rs.getString("name"));
			j.setClassId(rs.getInt("class_id"));
			j.setClassName(rs.getString("class_name"));
			j.setInherit(rs.getInt("inherit"));
			j.setRequiredLevel(rs.getInt("required_level"));
			return j;
		});
	}

	@Override
	public Job getJobById(Integer id) {
		List<Job> list = jdbcTemplate.query("SELECT * FROM m_jobs WHERE id = ?", new Object[] { id }, (rs, i) -> {
			Job j = new Job();
			j.setId(rs.getInt("id"));
			j.setName(rs.getString("name"));
			j.setClassId(rs.getInt("class_id"));
			j.setClassName(rs.getString("class_name"));
			j.setInherit(rs.getInt("inherit"));
			j.setRequiredLevel(rs.getInt("required_level"));
			return j;
		});
		return list.isEmpty() ? null : list.get(0);
	}

	@Override
	public List<Integer> getNextClassIds(Integer jobId) {
		return jdbcTemplate.queryForList("SELECT next_class_id FROM m_job_next_classes WHERE job_id = ?", Integer.class,
				jobId);
	}

	@Override
	public void saveJob(Job job) {
		if (job.getId() == null && job.getIsAdd()) {
			jdbcTemplate.update(
					"INSERT INTO m_jobs(name, class_id, class_name, inherit, required_level) VALUES(?,?,?,?,?)",
					job.getName(), job.getClassId(), job.getClassName(), job.getInherit(), job.getRequiredLevel());
		}if (job.getId() != null && job.getIsAdd()) {
			jdbcTemplate.update(
					"INSERT INTO m_jobs(id, name, class_id, class_name, inherit, required_level) VALUES(?,?,?,?,?,?)",
					job.getId() ,job.getName(), job.getClassId(), job.getClassName(), job.getInherit(), job.getRequiredLevel());
		} else {
			jdbcTemplate.update(
					"UPDATE m_jobs SET name=?, class_id=?, class_name=?, inherit=?, required_level=? WHERE id=?",
					job.getName(), job.getClassId(), job.getClassName(), job.getInherit(), job.getRequiredLevel(),
					job.getId());
		}
	}

	@Override
	public void deleteNextClasses(Integer jobId) {
		jdbcTemplate.update("DELETE FROM m_job_next_classes WHERE job_id = ?", jobId);
	}

	@Override
	public void insertNextClass(Integer jobId, Integer nextClassId) {
		jdbcTemplate.update("INSERT INTO m_job_next_classes(job_id, next_class_id) VALUES(?,?)", jobId, nextClassId);
	}

	@Override
	public void deleteJob(Integer id) {
		jdbcTemplate.update("DELETE FROM m_job_next_classes WHERE job_id = ?", id);
		jdbcTemplate.update("DELETE FROM m_jobs WHERE id = ?", id);
	}
}
