package com.dnc.simulator.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dnc.simulator.model.SuffixItem;
import com.dnc.simulator.model.SuffixItemExtraStat;
import com.dnc.simulator.service.EquipmentItemService;
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

	public MasterSuffixItemController(SuffixItemService suffixItemService, EquipmentItemService equipmentItemService,
			SuffixService suffixService, StatService statService, SuffixItemStatService suffixItemStatService) {

		this.suffixItemService = suffixItemService;
		this.equipmentItemService = equipmentItemService;
		this.suffixService = suffixService;
		this.statService = statService;
		this.suffixItemStatService = suffixItemStatService;
	}

	/* ================= LIST ================= */
	@GetMapping
	public String list(Model model) {

		model.addAttribute("items", suffixItemService.getAll());

		// equipment item map (itemId -> name)
		Map<Long, String> equipmentItemMap = new HashMap<>();
		equipmentItemService.getAll().forEach(e -> equipmentItemMap.put(e.getItemId(), e.getName()));
		model.addAttribute("equipmentItemMap", equipmentItemMap);

		// suffix type map (suffixTypeId -> name)
		Map<Integer, String> suffixTypeMap = new HashMap<>();
		suffixService.getAllSuffixTypes().forEach(t -> suffixTypeMap.put(t.getSuffixId(), t.getSuffixName()));
		model.addAttribute("suffixTypeMap", suffixTypeMap);

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

		// 2. โหลด extra stats จาก DB
		List<SuffixItemExtraStat> extraStats = suffixItemStatService.getExtraStats(suffixItemId);

		// 3. ถ้ายังไม่มี → load default
		if (extraStats == null || extraStats.isEmpty()) {
			extraStats = suffixItemStatService.buildDefaultExtraStats(suffixItem.getSuffixTypeId());
		}

		// 4. master stats
		model.addAttribute("suffixItemId", suffixItemId);
		model.addAttribute("itemId", suffixItem.getItemId());
		model.addAttribute("stats", statService.getAllStats());
		model.addAttribute("extraStats", extraStats);

		// 5. layout
		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/suffix-item-stats-form.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "suffix-items");

		return "layout/main";
	}

}
