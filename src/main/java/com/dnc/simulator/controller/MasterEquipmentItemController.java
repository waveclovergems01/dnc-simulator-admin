package com.dnc.simulator.controller;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.dnc.simulator.model.EquipmentItem;
import com.dnc.simulator.model.EquipmentItemStat;
import com.dnc.simulator.service.*;

@Controller
public class MasterEquipmentItemController {

	private final EquipmentItemService equipmentService;
	private final ItemTypeService itemTypeService;
	private final JobService jobService;
	private final RarityService rarityService;
	private final StatService statService;

	public MasterEquipmentItemController(EquipmentItemService equipmentService, ItemTypeService itemTypeService,
			JobService jobService, RarityService rarityService, StatService statService) {

		this.equipmentService = equipmentService;
		this.itemTypeService = itemTypeService;
		this.jobService = jobService;
		this.rarityService = rarityService;
		this.statService = statService;
	}

	/* ========================= LIST ========================= */
	@GetMapping("/master/equipment")
	public String list(Model model) {

		List<EquipmentItem> items = equipmentService.getAll();
		model.addAttribute("items", items);

		model.addAttribute("itemTypes", itemTypeService.getAllItemTypes());
		model.addAttribute("jobs", jobService.getAllJobs());
		model.addAttribute("rarities", rarityService.getAllRarities());

		// ✅ FIX: distinct set ids
		Set<Integer> setIds = new LinkedHashSet<>();
		for (EquipmentItem e : items) {
			if (e.getSetId() != null) {
				setIds.add(e.getSetId());
			}
		}
		model.addAttribute("setIds", setIds);

		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/equipment.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "equipment");

		return "layout/main";
	}

	/* ========================= ADD ========================= */
	@GetMapping("/master/equipment/add")
	public String addForm(Model model) {

		model.addAttribute("item", new EquipmentItem());
		model.addAttribute("itemTypes", itemTypeService.getAllItemTypes());
		model.addAttribute("jobs", jobService.getAllJobs());
		model.addAttribute("rarities", rarityService.getAllRarities());
		model.addAttribute("stats", statService.getAllStats());
		model.addAttribute("isAdd", true);

		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/equipment-form.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "equipment");

		return "layout/main";
	}

	/* ========================= EDIT ========================= */
	@GetMapping("/master/equipment/edit")
	public String editForm(@RequestParam Long itemId, Model model) {

		model.addAttribute("item", equipmentService.getById(itemId));
		model.addAttribute("itemTypes", itemTypeService.getAllItemTypes());
		model.addAttribute("jobs", jobService.getAllJobs());
		model.addAttribute("rarities", rarityService.getAllRarities());
		model.addAttribute("stats", statService.getAllStats());
		model.addAttribute("isAdd", false);

		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/equipment-form.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "equipment");

		return "layout/main";
	}

	@GetMapping("/master/equipment/clone")
	public String cloneForm(@RequestParam Long itemId, Model model) {

		EquipmentItem original = equipmentService.getById(itemId);

		if (original == null) {
			return "redirect:/master/equipment";
		}

		// ❗ สำคัญ: reset itemId เพื่อบังคับกรอกใหม่
		original.setItemId(null);

		model.addAttribute("item", original);
		model.addAttribute("itemTypes", itemTypeService.getAllItemTypes());
		model.addAttribute("jobs", jobService.getAllJobs());
		model.addAttribute("rarities", rarityService.getAllRarities());
		model.addAttribute("stats", statService.getAllStats());

		// clone = add
		model.addAttribute("isAdd", true);

		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/equipment-form.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "equipment");

		return "layout/main";
	}

	/* ========================= SAVE ========================= */
	@PostMapping("/master/equipment/save")
	public String save(@ModelAttribute EquipmentItem item, @RequestParam boolean isAdd) {
		if (item != null && item.getItemId() != null && item.getStats() != null && !item.getStats().isEmpty()) {
			for (EquipmentItemStat temp : item.getStats()) {
				temp.setItemId(item.getItemId());
				if (temp.getIsPercentage() == null) {
					temp.setIsPercentage(0);
				}
			}
		}

		equipmentService.save(item, isAdd);
		return "redirect:/master/equipment";
	}

	/* ========================= DELETE ========================= */
	@PostMapping("/master/equipment/delete")
	public String delete(@RequestParam Long itemId) {

		equipmentService.delete(itemId);
		return "redirect:/master/equipment";
	}
}
