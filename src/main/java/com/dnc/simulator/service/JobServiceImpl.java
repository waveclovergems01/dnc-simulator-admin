package com.dnc.simulator.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dnc.simulator.model.Job;
import com.dnc.simulator.repository.JobRepository;

@Service
@Transactional
public class JobServiceImpl implements JobService {

	private final JobRepository jobRepository;

	public JobServiceImpl(JobRepository jobRepository) {
		this.jobRepository = jobRepository;
	}

	@Override
	public List<Job> getAllJobs() {
		return jobRepository.findAll();
	}

	@Override
	public Job getById(int id) {
		return jobRepository.findById(id);
	}

	@Override
	public void insert(Job job) {
		jobRepository.insert(job);
	}

	@Override
	public void delete(int id) {
		jobRepository.delete(id);
	}
}
