package com.dnc.simulator.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dnc.simulator.model.SuffixItem;
import com.dnc.simulator.repository.SuffixItemRepository;

@Service
public class SuffixItemServiceImpl implements SuffixItemService {

	private final SuffixItemRepository suffixItemRepository;

	public SuffixItemServiceImpl(SuffixItemRepository suffixItemRepository) {
		this.suffixItemRepository = suffixItemRepository;
	}

	@Override
	public List<SuffixItem> getAll() {
		return suffixItemRepository.findAll();
	}

	@Override
	public SuffixItem getById(Long id) {
		return suffixItemRepository.findById(id);
	}

	@Override
	public List<SuffixItem> getByItemId(Long itemId) {
		return suffixItemRepository.findByItemId(itemId);
	}

	@Override
	public void save(SuffixItem item) {
		suffixItemRepository.save(item);
	}

	@Override
	public Long saveAndReturnId(SuffixItem item) {
		return suffixItemRepository.saveAndReturnId(item);
	}

	@Override
	public void delete(Long id) {
		suffixItemRepository.delete(id);
	}
}
