package com.dnc.simulator.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dnc.simulator.model.*;
import com.dnc.simulator.repository.SuffixRepository;
import com.dnc.simulator.service.SuffixService;

@Service
public class SuffixServiceImpl implements SuffixService {

	private final SuffixRepository suffixRepository;

	public SuffixServiceImpl(SuffixRepository suffixRepository) {
		this.suffixRepository = suffixRepository;
	}

	/* ===================== Suffix Types ===================== */

	@Override
	public List<SuffixType> getAllSuffixTypes() {
		return suffixRepository.findAllSuffixTypes();
	}

	@Override
	public SuffixType getSuffixTypeById(Integer suffixId) {
		return suffixRepository.findSuffixTypeById(suffixId);
	}

	@Override
	public void createSuffixType(SuffixType suffixType) {
		suffixRepository.insertSuffixType(suffixType);
	}

	@Override
	public void updateSuffixType(SuffixType suffixType) {
		suffixRepository.updateSuffixType(suffixType);
	}

	@Override
	public void deleteSuffixType(Integer suffixId) {
		suffixRepository.deleteSuffixType(suffixId);
	}

	/* ===================== Suffix Groups ===================== */

	@Override
	public List<SuffixGroup> getAllGroups() {
		return suffixRepository.findAllGroups();
	}

	@Override
	public SuffixGroup getGroupById(Integer groupId) {
		return suffixRepository.findGroupById(groupId);
	}

	@Override
	public void createGroup(SuffixGroup group) {
		suffixRepository.insertGroup(group);
	}

	@Override
	public void updateGroup(SuffixGroup group) {
		suffixRepository.updateGroup(group);
	}

	@Override
	public void deleteGroup(Integer groupId) {
		suffixRepository.deleteGroup(groupId);
	}

	/* ===================== Suffix Group Items ===================== */

	@Override
	public List<SuffixGroupItem> getAllGroupItems() {
		return suffixRepository.findAllGroupItems();
	}

	@Override
	public List<SuffixGroupItem> getGroupItemsByGroupId(Integer groupId) {
		return suffixRepository.findGroupItemsByGroupId(groupId);
	}

	@Override
	public SuffixGroupItem getGroupItemById(Integer id) {
		return suffixRepository.findGroupItemById(id);
	}

	@Override
	public void createGroupItem(SuffixGroupItem item) {
		suffixRepository.insertGroupItem(item);
	}

	@Override
	public void updateGroupItem(SuffixGroupItem item) {
		suffixRepository.updateGroupItem(item);
	}

	@Override
	public void deleteGroupItem(Integer id) {
		suffixRepository.deleteGroupItem(id);
	}
}
