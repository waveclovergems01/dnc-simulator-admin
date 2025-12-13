package com.dnc.simulator.repository;

import com.dnc.simulator.model.Job;
import java.util.List;

public interface JobRepository {

	List<Job> getAllJobs();

	Job getJobById(Integer id);

	List<Integer> getNextClassIds(Integer jobId);

	void saveJob(Job job);

	void deleteNextClasses(Integer jobId);

	void insertNextClass(Integer jobId, Integer nextClassId);

	void deleteJob(Integer id);
}
