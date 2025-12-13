package com.dnc.simulator.repository;

import java.util.List;

import com.dnc.simulator.model.Job;

public interface JobRepository {
	List<Job> findAll();

	Job findById(int id);

	void insert(Job job);

	void delete(int id);
}
