package com.dnc.simulator.service;

import java.util.List;
import com.dnc.simulator.model.*;

public interface SuffixService {

	/* Suffix Types */
	List<SuffixType> getAllSuffixTypes();

	SuffixType getSuffixTypeById(Integer suffixId);

	void createSuffixType(SuffixType suffixType);

	void updateSuffixType(SuffixType suffixType);

	void deleteSuffixType(Integer suffixId);

	/* Suffix Groups */
	List<SuffixGroup> getAllGroups();

	SuffixGroup getGroupById(Integer groupId);

	void createGroup(SuffixGroup group);

	void updateGroup(SuffixGroup group);

	void deleteGroup(Integer groupId);

	/* Suffix Group Items */
	List<SuffixGroupItem> getAllGroupItems();

	List<SuffixGroupItem> getGroupItemsByGroupId(Integer groupId);

	SuffixGroupItem getGroupItemById(Integer id);

	void createGroupItem(SuffixGroupItem item);

	void updateGroupItem(SuffixGroupItem item);

	void deleteGroupItem(Integer id);
}
