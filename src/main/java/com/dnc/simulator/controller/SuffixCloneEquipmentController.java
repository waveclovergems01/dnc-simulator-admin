package com.dnc.simulator.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dnc.simulator.model.Job;
import com.dnc.simulator.model.SuffixItem;
import com.dnc.simulator.model.SuffixItemAbility;
import com.dnc.simulator.model.SuffixItemAbilityStat;
import com.dnc.simulator.model.SuffixItemExtraStat;
import com.dnc.simulator.model.SuffixType;
import com.dnc.simulator.model.equipment.EquipmentItem;
import com.dnc.simulator.service.JobService;
import com.dnc.simulator.service.RarityService;
import com.dnc.simulator.service.SuffixCloneService;
import com.dnc.simulator.service.SuffixItemService;
import com.dnc.simulator.service.SuffixService;
import com.dnc.simulator.service.equipment.EquipmentItemService;

@Controller
@RequestMapping("/master/suffix-items")
public class SuffixCloneEquipmentController {

	private final EquipmentItemService equipmentItemService;
	private final SuffixItemService suffixItemService;
	private final SuffixCloneService suffixCloneService;
	private final EquipmentItemService equipmentService;
	private final SuffixService suffixService;
	private final JobService jobService;
	private final RarityService rarityService;

	public SuffixCloneEquipmentController(EquipmentItemService equipmentItemService,
			SuffixItemService suffixItemService, SuffixCloneService suffixCloneService,
			EquipmentItemService equipmentService, SuffixService suffixService, JobService jobService,
			RarityService rarityService) {

		this.equipmentItemService = equipmentItemService;
		this.suffixItemService = suffixItemService;
		this.suffixCloneService = suffixCloneService;
		this.equipmentService = equipmentService;
		this.suffixService = suffixService;
		this.jobService = jobService;
		this.rarityService = rarityService;
	}

	/*
	 * ========================================================= CLONE SUFFIX –
	 * SELECT EQUIPMENT =========================================================
	 */
	@GetMapping("/clone-suffix")
	public String clonePage(@RequestParam(required = false) Long equipmentItemId,
			@RequestParam(required = false) Integer sourceJobFilter,
			@RequestParam(required = false) Integer sourceLevelFilter,
			@RequestParam(required = false) Integer sourceRarityFilter, Model model) {

		List<EquipmentItem> allEquipments = equipmentItemService.getAll();

		List<EquipmentItem> sourceEquipments = new ArrayList<>();
		List<EquipmentItem> targetEquipments = new ArrayList<>();

		for (EquipmentItem e : allEquipments) {

			List<SuffixItem> suffixList = suffixItemService.getByItemId(e.getItemId());

			if (suffixList != null && !suffixList.isEmpty()) {
				sourceEquipments.add(e);
			} else {
				targetEquipments.add(e);
			}
		}

		sortEquipmentList(sourceEquipments);
		sortEquipmentList(targetEquipments);

		model.addAttribute("sourceEquipments", sourceEquipments);
		model.addAttribute("targetEquipments", targetEquipments);

		prepareFilterModel(model, sourceEquipments, "source");
		prepareFilterModel(model, targetEquipments, "target");

		model.addAttribute("selectedSourceJobFilter", sourceJobFilter);
		model.addAttribute("selectedSourceLevelFilter", sourceLevelFilter);
		model.addAttribute("selectedSourceRarityFilter", sourceRarityFilter);

		if (equipmentItemId != null) {

			EquipmentItem item = equipmentItemService.getById(equipmentItemId);

			if (item != null) {
				List<SuffixItem> suffixes = suffixItemService.getByItemId(equipmentItemId);

				model.addAttribute("equipmentItem", item);
				model.addAttribute("suffixes", suffixes);
			}
		}

		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/suffix-clone-suffix.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "suffix-items");

		return "layout/main";
	}

	private void sortEquipmentList(List<EquipmentItem> items) {
		Collections.sort(items, new Comparator<EquipmentItem>() {
			@Override
			public int compare(EquipmentItem a, EquipmentItem b) {
				String an = a.getName() == null ? "" : a.getName();
				String bn = b.getName() == null ? "" : b.getName();

				int nameCompare = an.compareToIgnoreCase(bn);
				if (nameCompare != 0) {
					return nameCompare;
				}

				Long aid = a.getItemId() == null ? 0L : a.getItemId();
				Long bid = b.getItemId() == null ? 0L : b.getItemId();
				return aid.compareTo(bid);
			}
		});
	}

	private void prepareFilterModel(Model model, List<EquipmentItem> items, String prefix) {

		Map<Integer, String> allJobMap = new LinkedHashMap<>();
		for (Job j : jobService.getAllJobs()) {
			allJobMap.put(j.getId(), j.getName());
		}

		Map<Integer, String> allRarityMap = new LinkedHashMap<>();
		rarityService.getAllRarities().forEach(r -> allRarityMap.put(r.getRarityId(), r.getRarityName()));

		Set<Integer> usedJobIds = new LinkedHashSet<>();
		Set<Integer> usedLevels = new LinkedHashSet<>();
		Set<Integer> usedRarityIds = new LinkedHashSet<>();

		for (EquipmentItem e : items) {
			if (e.getJobId() != null) {
				usedJobIds.add(e.getJobId());
			}
			if (e.getRequiredLevel() != null) {
				usedLevels.add(e.getRequiredLevel());
			}
			if (e.getRarityId() != null) {
				usedRarityIds.add(e.getRarityId());
			}
		}

		Map<Integer, String> jobFilterMap = new LinkedHashMap<>();
		for (Map.Entry<Integer, String> entry : allJobMap.entrySet()) {
			// if (usedJobIds.contains(entry.getKey())) {
			// jobFilterMap.put(entry.getKey(), entry.getValue());
			// }
			jobFilterMap.put(entry.getKey(), entry.getValue());
		}

		List<Integer> levelFilterList = new ArrayList<>(usedLevels);
		Collections.sort(levelFilterList);

		Map<Integer, String> rarityFilterMap = new LinkedHashMap<>();
		for (Map.Entry<Integer, String> entry : allRarityMap.entrySet()) {
			if (usedRarityIds.contains(entry.getKey())) {
				rarityFilterMap.put(entry.getKey(), entry.getValue());
			}
		}

		model.addAttribute(prefix + "JobFilterMap", jobFilterMap);
		model.addAttribute(prefix + "LevelFilterList", levelFilterList);
		model.addAttribute(prefix + "RarityFilterMap", rarityFilterMap);
	}

	/*
	 * ========================================================= CLONE SUFFIX – SAVE
	 * =========================================================
	 */
	@PostMapping("/clone-suffix/save")
	public String cloneSave(@RequestParam Long originalEquipmentItemId, @RequestParam Long newEquipmentItemId,
			@RequestParam("suffixItemId") List<Long> suffixItemIds) {

		Map<Long, SuffixItem> clones = new LinkedHashMap<>();

		for (Long suffixItemId : suffixItemIds) {

			SuffixItem original = suffixItemService.getById(suffixItemId);
			if (original == null) {
				continue;
			}

			EquipmentItem newEquipmentItem = equipmentService.getById(newEquipmentItemId);
			SuffixType newSuffixType = suffixService.getSuffixTypeById(original.getSuffixTypeId());
			
			String suffixDisplay = formatSuffix(newSuffixType.getSuffixName(), original.getTier());

			SuffixItem clone = new SuffixItem();
			clone.setId(null);
			clone.setItemId(newEquipmentItemId);
			clone.setName(newEquipmentItem.getName() + " " + suffixDisplay);
			clone.setSuffixTypeId(original.getSuffixTypeId());
			clone.setTier(original.getTier());

			if (original.getExtraStats() != null && !original.getExtraStats().isEmpty()) {

				List<SuffixItemExtraStat> clonedExtraStats = new ArrayList<>();

				for (SuffixItemExtraStat es : original.getExtraStats()) {

					SuffixItemExtraStat esClone = new SuffixItemExtraStat();
					esClone.setId(null);
					esClone.setSuffixItemId(null);
					esClone.setStatId(es.getStatId());
					esClone.setValueMin(es.getValueMin());
					esClone.setValueMax(es.getValueMax());
					esClone.setIsPercentage(es.getIsPercentage());

					clonedExtraStats.add(esClone);
				}

				clone.setExtraStats(clonedExtraStats);
			}

			if (original.getAbilities() != null && !original.getAbilities().isEmpty()) {

				List<SuffixItemAbility> clonedAbilities = new ArrayList<>();

				for (SuffixItemAbility ab : original.getAbilities()) {

					SuffixItemAbility abClone = new SuffixItemAbility();
					abClone.setAbilityId(null);
					abClone.setSuffixItemId(null);
					abClone.setRawText(ab.getRawText());
					abClone.setType(ab.getType());

					if (ab.getAbilityStats() != null && !ab.getAbilityStats().isEmpty()) {

						List<SuffixItemAbilityStat> clonedStats = new ArrayList<>();

						for (SuffixItemAbilityStat st : ab.getAbilityStats()) {

							SuffixItemAbilityStat stClone = new SuffixItemAbilityStat();
							stClone.setId(null);
							stClone.setAbilityId(null);
							stClone.setStatId(st.getStatId());
							stClone.setValueMin(st.getValueMin());
							stClone.setValueMax(st.getValueMax());
							stClone.setIsPercentage(st.getIsPercentage());

							clonedStats.add(stClone);
						}

						abClone.setAbilityStats(clonedStats);
					}

					clonedAbilities.add(abClone);
				}

				clone.setAbilities(clonedAbilities);
			}

			clones.put(suffixItemId, clone);
		}

		int success = suffixCloneService.cloneEquipment(clones);

		return "redirect:/master/suffix-items?cloned=" + success;
	}

	public String formatSuffix(String suffixName, int tier) {
		String roman;
		switch (tier) {
		case 1:
			roman = "";
			break;
		case 2:
			roman = " II";
			break;
		case 3:
			roman = " III";
			break;
		case 4:
			roman = " IV";
			break;
		default:
			roman = "";
			break;
		}

		return String.format("(%s%s)", suffixName, roman);
	}
}