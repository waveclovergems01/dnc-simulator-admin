package com.dnc.simulator.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dnc.simulator.model.Job;
import com.dnc.simulator.model.SuffixItem;
import com.dnc.simulator.model.SuffixItemAbility;
import com.dnc.simulator.model.SuffixItemExtraStat;
import com.dnc.simulator.model.equipment.EquipmentItem;
import com.dnc.simulator.service.JobService;
import com.dnc.simulator.service.RarityService;
import com.dnc.simulator.service.StatService;
import com.dnc.simulator.service.SuffixItemService;
import com.dnc.simulator.service.SuffixItemStatService;
import com.dnc.simulator.service.SuffixService;
import com.dnc.simulator.service.equipment.EquipmentItemService;

@Controller
@RequestMapping("/master/suffix-items")
public class MasterSuffixItemController {

	private final SuffixItemService suffixItemService;
	private final SuffixItemStatService suffixItemStatService;
	private final EquipmentItemService equipmentItemService;
	private final SuffixService suffixService;
	private final StatService statService;
	private final JobService jobService;
	private final RarityService rarityService;

	public MasterSuffixItemController(
			SuffixItemService suffixItemService,
			EquipmentItemService equipmentItemService,
			SuffixService suffixService,
			StatService statService,
			SuffixItemStatService suffixItemStatService,
			JobService jobService,
			RarityService rarityService) {

		this.suffixItemService = suffixItemService;
		this.equipmentItemService = equipmentItemService;
		this.suffixService = suffixService;
		this.statService = statService;
		this.suffixItemStatService = suffixItemStatService;
		this.jobService = jobService;
		this.rarityService = rarityService;
	}

	/* ================= LIST ================= */
	@GetMapping
	public String list(Model model) {

		List<SuffixItem> suffixItems = suffixItemService.getAll();
		model.addAttribute("items", suffixItems);

		List<SuffixItem> filterSuffixItems = suffixItems.stream()
				.collect(Collectors.toMap(
						SuffixItem::getItemId,
						s -> s,
						(oldVal, newVal) -> oldVal,
						LinkedHashMap::new))
				.values()
				.stream()
				.collect(Collectors.toList());

		model.addAttribute("filterSuffixItems", filterSuffixItems);

		Map<Long, String> equipmentItemMap = new HashMap<Long, String>();
		equipmentItemService.getAll().forEach(e -> equipmentItemMap.put(e.getItemId(), e.getName()));
		model.addAttribute("equipmentItemMap", equipmentItemMap);

		Map<Integer, String> suffixTypeMap = new HashMap<Integer, String>();
		suffixService.getAllSuffixTypes().forEach(t -> suffixTypeMap.put(t.getSuffixId(), t.getSuffixName()));
		model.addAttribute("suffixTypeMap", suffixTypeMap);

		Map<Integer, String> jobMap = jobService.getAllJobs().stream()
				.sorted(Comparator.comparingInt(Job::getId))
				.collect(Collectors.toMap(
						Job::getId,
						Job::getName,
						(a, b) -> a,
						LinkedHashMap::new));

		Map<Long, String> itemJobMap = new HashMap<Long, String>();
		equipmentItemService.getAll().forEach(e -> itemJobMap.put(e.getItemId(), jobMap.get(e.getJobId())));
		model.addAttribute("itemJobMap", itemJobMap);

		Map<Integer, String> jobFilterMap = new LinkedHashMap<Integer, String>();
		equipmentItemService.getAll().forEach(e -> jobFilterMap.putIfAbsent(e.getJobId(), jobMap.get(e.getJobId())));
		model.addAttribute("jobFilterMap", jobFilterMap);

		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/suffix-items.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "suffix-items");

		return "layout/main";
	}

	/* ================= ADD ================= */
	@GetMapping("/add")
	public String addForm(
			@RequestParam(name = "jobFilter", required = false) Integer jobFilter,
			@RequestParam(name = "levelFilter", required = false) Integer levelFilter,
			@RequestParam(name = "rarityFilter", required = false) Integer rarityFilter,
			Model model) {

		prepareCommonFormModel(model, null, jobFilter, levelFilter, rarityFilter);

		model.addAttribute("existingSuffixes", Collections.emptyList());
		model.addAttribute("suffixByTier", Collections.emptyMap());
		model.addAttribute("tiers", Collections.emptyList());
		model.addAttribute("selectedItemId", null);

		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/suffix-item-form.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "suffix-items");

		return "layout/main";
	}

	/* ================= EDIT ================= */
	@GetMapping("/edit")
	public String manageByItem(
			@RequestParam(name = "itemId", required = false) Long itemId,
			@RequestParam(name = "jobFilter", required = false) Integer jobFilter,
			@RequestParam(name = "levelFilter", required = false) Integer levelFilter,
			@RequestParam(name = "rarityFilter", required = false) Integer rarityFilter,
			Model model) {

		prepareCommonFormModel(model, itemId, jobFilter, levelFilter, rarityFilter);

		List<SuffixItem> existingSuffixes;

		if (itemId != null) {
			model.addAttribute("selectedItemId", itemId);
			existingSuffixes = suffixItemService.getByItemId(itemId);
		} else {
			model.addAttribute("selectedItemId", null);
			existingSuffixes = Collections.emptyList();
		}

		model.addAttribute("existingSuffixes", existingSuffixes);

		Map<Integer, List<SuffixItem>> suffixByTier = new LinkedHashMap<Integer, List<SuffixItem>>();

		for (SuffixItem s : existingSuffixes) {
			Integer tier = s.getTier();
			suffixByTier.computeIfAbsent(tier, k -> new ArrayList<SuffixItem>()).add(s);
		}

		List<Integer> supportedTiers = Arrays.asList(1, 2, 3);

		Set<Integer> tiers = new LinkedHashSet<Integer>();
		tiers.addAll(supportedTiers);
		tiers.addAll(suffixByTier.keySet());

		model.addAttribute("suffixByTier", suffixByTier);
		model.addAttribute("tiers", tiers);

		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/suffix-item-form.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "suffix-items");

		return "layout/main";
	}

	private void prepareCommonFormModel(
			Model model,
			Long selectedItemId,
			Integer selectedJobFilter,
			Integer selectedLevelFilter,
			Integer selectedRarityFilter) {

		List<EquipmentItem> equipmentItems = new ArrayList<EquipmentItem>(equipmentItemService.getAll());

		equipmentItems.sort(new Comparator<EquipmentItem>() {
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

		model.addAttribute("equipmentItems", equipmentItems);
		model.addAttribute("suffixTypes", suffixService.getAllSuffixTypes());

		Map<Integer, String> suffixTypeMap = new HashMap<Integer, String>();
		suffixService.getAllSuffixTypes().forEach(t -> suffixTypeMap.put(t.getSuffixId(), t.getSuffixName()));
		model.addAttribute("suffixTypeMap", suffixTypeMap);

		Map<Integer, String> jobMap = jobService.getAllJobs().stream()
				.sorted(Comparator.comparingInt(Job::getId))
				.collect(Collectors.toMap(
						Job::getId,
						Job::getName,
						(a, b) -> a,
						LinkedHashMap::new));
		model.addAttribute("jobMap", jobMap);

		Set<Integer> usedJobIds = new LinkedHashSet<Integer>();
		for (EquipmentItem e : equipmentItems) {
			if (e.getJobId() != null) {
				usedJobIds.add(e.getJobId());
			}
		}

		Map<Integer, String> jobFilterMap = new LinkedHashMap<Integer, String>();
		for (Map.Entry<Integer, String> entry : jobMap.entrySet()) {
			if (usedJobIds.contains(entry.getKey())) {
				jobFilterMap.put(entry.getKey(), entry.getValue());
			}
		}
		model.addAttribute("jobFilterMap", jobFilterMap);

		Set<Integer> usedLevels = new LinkedHashSet<Integer>();
		for (EquipmentItem e : equipmentItems) {
			if (e.getRequiredLevel() != null) {
				usedLevels.add(e.getRequiredLevel());
			}
		}
		List<Integer> levelFilterList = new ArrayList<Integer>(usedLevels);
		Collections.sort(levelFilterList);
		model.addAttribute("levelFilterList", levelFilterList);

		Map<Integer, String> rarityMap = new LinkedHashMap<Integer, String>();
		rarityService.getAllRarities().forEach(r -> rarityMap.put(r.getRarityId(), r.getRarityName()));
		model.addAttribute("rarityMap", rarityMap);

		Set<Integer> usedRarityIds = new LinkedHashSet<Integer>();
		for (EquipmentItem e : equipmentItems) {
			if (e.getRarityId() != null) {
				usedRarityIds.add(e.getRarityId());
			}
		}

		Map<Integer, String> rarityFilterMap = new LinkedHashMap<Integer, String>();
		for (Map.Entry<Integer, String> entry : rarityMap.entrySet()) {
			if (usedRarityIds.contains(entry.getKey())) {
				rarityFilterMap.put(entry.getKey(), entry.getValue());
			}
		}
		model.addAttribute("rarityFilterMap", rarityFilterMap);

		model.addAttribute("selectedItemId", selectedItemId);
		model.addAttribute("selectedJobFilter", selectedJobFilter);
		model.addAttribute("selectedLevelFilter", selectedLevelFilter);
		model.addAttribute("selectedRarityFilter", selectedRarityFilter);
	}

	/* ================= SAVE ================= */
	@PostMapping(value = "/save", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveSuffix(
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam("itemId") Long itemId,
			@RequestParam("suffixTypeId") Integer suffixTypeId,
			@RequestParam("tier") Integer tier,
			@RequestParam("name") String name) {

		if (itemId == null || suffixTypeId == null || tier == null || name == null || name.trim().isEmpty()) {
			return "{\"success\":false,\"message\":\"Invalid data\"}";
		}

		SuffixItem item = new SuffixItem();
		item.setId(id);
		item.setItemId(itemId);
		item.setSuffixTypeId(suffixTypeId);
		item.setTier(tier);
		item.setName(name);

		Long savedId = suffixItemService.saveAndReturnId(item);

		return "{\"success\":true,\"id\":" + savedId + "}";
	}

	/* ================= DELETE ================= */
	@PostMapping("/delete")
	public String delete(@RequestParam Long id) {
		suffixItemStatService.deleteExtraStatsBySuffixItemId(id.intValue());
		suffixItemStatService.deleteAbilityBySuffixItemId(id.intValue());
		suffixItemService.delete(id);
		return "redirect:/master/suffix-items";
	}

	/* ================= STATS ================= */
	@GetMapping("/stats")
	public String suffixItemStatsPage(@RequestParam("suffixItemId") Integer suffixItemId, Model model) {

		if (suffixItemId == null || suffixItemId <= 0) {
			throw new IllegalArgumentException("Invalid suffixItemId");
		}

		SuffixItem suffixItem = suffixItemService.getById(suffixItemId.longValue());

		if (suffixItem == null) {
			throw new IllegalArgumentException("Suffix item not found: " + suffixItemId);
		}

		List<SuffixItemExtraStat> extraStats = suffixItemStatService.getExtraStats(suffixItemId);

		if (extraStats == null || extraStats.isEmpty()) {
			extraStats = suffixItemStatService.buildDefaultExtraStats(suffixItem.getSuffixTypeId());
		}

		List<SuffixItemAbility> abilities = suffixItemStatService.getAbilitiesWithStatsBySuffixItemId(suffixItemId);

		SuffixItemAbility ability = abilities.isEmpty() ? new SuffixItemAbility() : abilities.get(0);

		model.addAttribute("suffixItemId", suffixItemId);
		model.addAttribute("itemId", suffixItem.getItemId());
		model.addAttribute("stats", statService.getAllStats());
		model.addAttribute("extraStats", extraStats);
		model.addAttribute("ability", ability);

		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/suffix-item-stats-form.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "suffix-items");

		return "layout/main";
	}

	@GetMapping("/clone-tier1-with-stats")
	public String cloneTier1WithStats(
			@RequestParam("itemId") Long itemId,
			@RequestParam("targetTier") Integer targetTier) {

		if (itemId == null || targetTier == null || targetTier <= 1) {
			throw new IllegalArgumentException("Invalid clone parameters");
		}

		suffixItemService.cloneTier1WithStats(itemId, targetTier);

		return "redirect:/master/suffix-items/edit?itemId=" + itemId;
	}
}