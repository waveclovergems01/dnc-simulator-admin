package com.dnc.simulator.repository;

import java.util.List;
import com.dnc.simulator.model.SuffixItem;

public interface SuffixItemRepository {

	List<SuffixItem> findAll();

	SuffixItem findById(Long id);

	List<SuffixItem> findByItemId(Long itemId);

	void save(SuffixItem item);
	
	public Long saveAndReturnId(SuffixItem item);

	void delete(Long id);
}
