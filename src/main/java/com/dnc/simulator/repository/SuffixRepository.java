package com.dnc.simulator.repository;

import java.util.List;
import com.dnc.simulator.model.*;

public interface SuffixRepository {

	/* ===================== Suffix Types ===================== */
	List<SuffixType> findAllSuffixTypes();

	SuffixType findSuffixTypeById(Integer suffixId);

	void insertSuffixType(SuffixType suffixType);

	void updateSuffixType(SuffixType suffixType);

	void deleteSuffixType(Integer suffixId);

	/* ===================== Suffix Groups ===================== */
	List<SuffixGroup> findAllGroups();

	SuffixGroup findGroupById(Integer groupId);

	void insertGroup(SuffixGroup group);

	void updateGroup(SuffixGroup group);

	void deleteGroup(Integer groupId);

	/* ===================== Suffix Group Items ===================== */
	List<SuffixGroupItem> findAllGroupItems();

	List<SuffixGroupItem> findGroupItemsByGroupId(Integer groupId);

	SuffixGroupItem findGroupItemById(Integer id);

	void insertGroupItem(SuffixGroupItem item);

	void updateGroupItem(SuffixGroupItem item);

	void deleteGroupItem(Integer id);
}
