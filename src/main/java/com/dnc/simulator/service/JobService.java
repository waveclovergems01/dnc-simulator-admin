package com.dnc.simulator.service;

import com.dnc.simulator.model.Job;
import java.util.List;

public interface JobService {

	List<Job> getAllJobs();

	Job getJobById(Integer id);

	List<Integer> getNextClassIds(Integer jobId);

	void saveJob(Job job, List<Integer> nextClassIds);

	void deleteJob(Integer id);
}
