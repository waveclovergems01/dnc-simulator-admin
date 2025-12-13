package com.dnc.simulator.service;

import com.dnc.simulator.model.Job;
import com.dnc.simulator.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class JobServiceImpl implements JobService {

	@Autowired
	private JobRepository jobRepository;

	@Override
	public List<Job> getAllJobs() {
		return jobRepository.getAllJobs();
	}

	@Override
	public Job getJobById(Integer id) {
		return jobRepository.getJobById(id);
	}

	@Override
	public List<Integer> getNextClassIds(Integer jobId) {
		return jobRepository.getNextClassIds(jobId);
	}

	@Override
	@Transactional
	public void saveJob(Job job, List<Integer> nextClassIds) {

		jobRepository.saveJob(job);

		if (job.getId() != null) {
			jobRepository.deleteNextClasses(job.getId());

			if (nextClassIds != null) {
				for (Integer nextId : nextClassIds) {
					if (!nextId.equals(job.getId())) {
						jobRepository.insertNextClass(job.getId(), nextId);
					}
				}
			}
		}
	}

	@Override
	public void deleteJob(Integer id) {
		jobRepository.deleteJob(id);
	}
}
