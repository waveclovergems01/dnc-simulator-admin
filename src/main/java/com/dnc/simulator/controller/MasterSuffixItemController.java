package com.dnc.simulator.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
import com.dnc.simulator.service.EquipmentItemService;
import com.dnc.simulator.service.JobService;
import com.dnc.simulator.service.StatService;
import com.dnc.simulator.service.SuffixItemService;
import com.dnc.simulator.service.SuffixItemStatService;
import com.dnc.simulator.service.SuffixService;

@Controller
@RequestMapping("/master/suffix-items")
public class MasterSuffixItemController {

	private final SuffixItemService suffixItemService;
	private final SuffixItemStatService suffixItemStatService;
	private final EquipmentItemService equipmentItemService;
	private final SuffixService suffixService;
	private final StatService statService;
	private final JobService jobService;

	public MasterSuffixItemController(SuffixItemService suffixItemService, EquipmentItemService equipmentItemService,
			SuffixService suffixService, StatService statService, SuffixItemStatService suffixItemStatService,
			JobService jobService) {

		this.suffixItemService = suffixItemService;
		this.equipmentItemService = equipmentItemService;
		this.suffixService = suffixService;
		this.statService = statService;
		this.suffixItemStatService = suffixItemStatService;
		this.jobService = jobService;
	}

	/* ================= LIST ================= */
	@GetMapping
	public String list(Model model) {

		List<SuffixItem> suffixItems = suffixItemService.getAll();
		model.addAttribute("items", suffixItems);

		// ===== distinct สำหรับ filter item =====
		List<SuffixItem> filterSuffixItems = suffixItems.stream()
				.collect(
						Collectors.toMap(SuffixItem::getItemId, s -> s, (oldVal, newVal) -> oldVal, LinkedHashMap::new))
				.values().stream().collect(Collectors.toList());

		model.addAttribute("filterSuffixItems", filterSuffixItems);

		// ===== equipment item map (itemId -> name) =====
		Map<Long, String> equipmentItemMap = new HashMap<>();
		equipmentItemService.getAll().forEach(e -> equipmentItemMap.put(e.getItemId(), e.getName()));
		model.addAttribute("equipmentItemMap", equipmentItemMap);

		// ===== suffix type map =====
		Map<Integer, String> suffixTypeMap = new HashMap<>();
		suffixService.getAllSuffixTypes().forEach(t -> suffixTypeMap.put(t.getSuffixId(), t.getSuffixName()));
		model.addAttribute("suffixTypeMap", suffixTypeMap);

		// ===== jobId -> jobName (sorted asc) =====
		Map<Integer, String> jobMap = jobService.getAllJobs().stream().sorted(Comparator.comparingInt(Job::getId))
				.collect(Collectors.toMap(Job::getId, Job::getName, (a, b) -> a, LinkedHashMap::new));

		// ===== itemId -> jobName (for table) =====
		Map<Long, String> itemJobMap = new HashMap<>();
		equipmentItemService.getAll().forEach(e -> itemJobMap.put(e.getItemId(), jobMap.get(e.getJobId())));
		model.addAttribute("itemJobMap", itemJobMap);

		// ===== job filter (distinct + sorted) =====
		Map<Integer, String> jobFilterMap = new LinkedHashMap<>();
		equipmentItemService.getAll().forEach(e -> jobFilterMap.putIfAbsent(e.getJobId(), jobMap.get(e.getJobId())));
		model.addAttribute("jobFilterMap", jobFilterMap);

		// =================================================

		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/suffix-items.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "suffix-items");

		return "layout/main";
	}

	/* ================= ADD ================= */
	@GetMapping("/add")
	public String addForm(Model model) {

		model.addAttribute("equipmentItems", equipmentItemService.getAll());
		model.addAttribute("suffixTypes", suffixService.getAllSuffixTypes());

		Map<Integer, String> suffixTypeMap = new HashMap<>();
		suffixService.getAllSuffixTypes().forEach(t -> suffixTypeMap.put(t.getSuffixId(), t.getSuffixName()));
		model.addAttribute("suffixTypeMap", suffixTypeMap);

		model.addAttribute("existingSuffixes", Collections.emptyList());
		model.addAttribute("selectedItemId", null);

		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/suffix-item-form.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "suffix-items");

		return "layout/main";
	}

	/* ================= EDIT ================= */
	@GetMapping("/edit")
	public String manageByItem(@RequestParam(name = "itemId", required = false) Long itemId, Model model) {

		// dropdown item
		model.addAttribute("equipmentItems", equipmentItemService.getAll());

		// suffix types (ใช้ทั้ง dropdown + map)
		model.addAttribute("suffixTypes", suffixService.getAllSuffixTypes());

		// map suffixTypeId -> name (แสดงใน table)
		Map<Integer, String> suffixTypeMap = new HashMap<>();
		suffixService.getAllSuffixTypes().forEach(t -> suffixTypeMap.put(t.getSuffixId(), t.getSuffixName()));
		model.addAttribute("suffixTypeMap", suffixTypeMap);

		if (itemId != null) {
			model.addAttribute("selectedItemId", itemId);
			model.addAttribute("existingSuffixes", suffixItemService.getByItemId(itemId));
		} else {
			model.addAttribute("selectedItemId", null);
			model.addAttribute("existingSuffixes", Collections.emptyList());
		}

		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/suffix-item-form.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "suffix-items");

		return "layout/main";
	}

	/* ================= SAVE ================= */
	@PostMapping(value = "/save", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveSuffix(@RequestParam(value = "id", required = false) Long id, @RequestParam("itemId") Long itemId,
			@RequestParam("suffixTypeId") Integer suffixTypeId, @RequestParam("name") String name) {

		if (itemId == null || suffixTypeId == null || name == null || name.trim().isEmpty()) {
			return "{\"success\":false,\"message\":\"Invalid data\"}";
		}

		SuffixItem item = new SuffixItem();
		item.setId(id);
		item.setItemId(itemId);
		item.setSuffixTypeId(suffixTypeId);
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

	@GetMapping("/stats")
	public String suffixItemStatsPage(@RequestParam("suffixItemId") Integer suffixItemId, Model model) {

		if (suffixItemId == null || suffixItemId <= 0) {
			throw new IllegalArgumentException("Invalid suffixItemId");
		}

		// 1. โหลด suffix item
		SuffixItem suffixItem = suffixItemService.getById(suffixItemId.longValue());

		if (suffixItem == null) {
			throw new IllegalArgumentException("Suffix item not found: " + suffixItemId);
		}

		/*
		 * ===================================================== EXTRA STATS (ของเดิม)
		 * =====================================================
		 */

		List<SuffixItemExtraStat> extraStats = suffixItemStatService.getExtraStats(suffixItemId);

		if (extraStats == null || extraStats.isEmpty()) {
			extraStats = suffixItemStatService.buildDefaultExtraStats(suffixItem.getSuffixTypeId());
		}

		/*
		 * ===================================================== ABILITY (ADD)
		 * =====================================================
		 */

		// ดึง ability ทั้งหมดของ suffix item นี้
		List<SuffixItemAbility> abilities = suffixItemStatService.getAbilitiesWithStatsBySuffixItemId(suffixItemId);

		// ใช้ ability แรก (กรณี 1 suffix item = 1 ability)
		SuffixItemAbility ability = abilities.isEmpty() ? new SuffixItemAbility() : abilities.get(0);

		/*
		 * ===================================================== MODEL
		 * =====================================================
		 */

		model.addAttribute("suffixItemId", suffixItemId);
		model.addAttribute("itemId", suffixItem.getItemId());

		model.addAttribute("stats", statService.getAllStats());
		model.addAttribute("extraStats", extraStats);

		model.addAttribute("ability", ability);

		/*
		 * ===================================================== LAYOUT
		 * =====================================================
		 */

		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/suffix-item-stats-form.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "suffix-items");

		return "layout/main";
	}

}
