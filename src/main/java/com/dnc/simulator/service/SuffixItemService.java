package com.dnc.simulator.service;

import java.util.List;
import com.dnc.simulator.model.SuffixItem;

public interface SuffixItemService {

	List<SuffixItem> getAll();

	SuffixItem getById(Long id);

	List<SuffixItem> getByItemId(Long itemId);

	void save(SuffixItem item);
	
	public Long saveAndReturnId(SuffixItem item);

	void delete(Long id);
}
