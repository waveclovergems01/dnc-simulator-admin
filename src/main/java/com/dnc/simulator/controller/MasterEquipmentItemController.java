package com.dnc.simulator.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.dnc.simulator.model.EquipmentItem;
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

	@GetMapping("/master/equipment")
	public String list(Model model) {

		model.addAttribute("items", equipmentService.getAll());

		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/equipment.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "equipment");

		return "layout/main";
	}

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

	@PostMapping("/master/equipment/save")
	public String save(@ModelAttribute EquipmentItem item, @RequestParam boolean isAdd) {

		equipmentService.save(item, isAdd);
		return "redirect:/master/equipment";
	}

	@PostMapping("/master/equipment/delete")
	public String delete(@RequestParam Long itemId) {

		equipmentService.delete(itemId);
		return "redirect:/master/equipment";
	}
}
