package com.dnc.simulator.service;

import java.util.List;

import com.dnc.simulator.model.Job;

public interface JobService {
	List<Job> getAllJobs();

	Job getById(int id);

	void insert(Job job);

	void delete(int id);
}
