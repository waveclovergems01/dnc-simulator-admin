package com.dnc.simulator.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dnc.simulator.model.Category;
import com.dnc.simulator.model.ItemType;
import com.dnc.simulator.model.Rarity;
import com.dnc.simulator.service.CategoryService;
import com.dnc.simulator.service.ItemTypeService;
import com.dnc.simulator.service.RarityService;

@Controller
public class MasterRarityController {

	private final RarityService rarityService;
	private final CategoryService categoryService;
	private final ItemTypeService itemTypeService;

	public MasterRarityController(RarityService rarityService, CategoryService categoryService,
			ItemTypeService itemTypeService) {

		this.rarityService = rarityService;
		this.categoryService = categoryService;
		this.itemTypeService = itemTypeService;
	}

	/*
	 * ========================= LIST =========================
	 */
	@GetMapping("/master/rarities")
	public String rarities(Model model) {

		List<Rarity> rarities = rarityService.getAllRarities();
		List<Category> categories = categoryService.getAllCategories();
		List<ItemType> itemTypes = itemTypeService.getAllItemTypes();

		// ---------- map rarityId -> categories ----------
		Map<Integer, List<Category>> categoriesByRarity = new HashMap<>();
		for (Rarity r : rarities) {
			List<Integer> categoryIds = rarityService.getCategoriesByRarity(r.getRarityId());

			List<Category> list = new ArrayList<>();
			for (Category c : categories) {
				if (categoryIds.contains(c.getCategoryId())) {
					list.add(c);
				}
			}
			categoriesByRarity.put(r.getRarityId(), list);
		}

		// ---------- map rarityId -> item types ----------
		Map<Integer, List<ItemType>> itemTypesByRarity = new HashMap<>();
		for (Rarity r : rarities) {
			List<Integer> typeIds = rarityService.getItemTypesByRarity(r.getRarityId());

			List<ItemType> list = new ArrayList<>();
			for (ItemType t : itemTypes) {
				if (typeIds.contains(t.getTypeId())) {
					list.add(t);
				}
			}
			itemTypesByRarity.put(r.getRarityId(), list);
		}

		model.addAttribute("rarities", rarities);
		model.addAttribute("categoriesByRarity", categoriesByRarity);
		model.addAttribute("itemTypesByRarity", itemTypesByRarity);

		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/rarity.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "rarities");

		return "layout/main";
	}

	/*
	 * ========================= ADD FORM =========================
	 */
	@GetMapping("/master/rarities/add")
	public String addForm(Model model) {

		model.addAttribute("rarity", new Rarity());
		model.addAttribute("categories", categoryService.getAllCategories());
		model.addAttribute("itemTypes", itemTypeService.getAllItemTypes());

		model.addAttribute("selectedCategoryRarityIds", new ArrayList<Integer>());
		model.addAttribute("selectedItemTypeRarityIds", new ArrayList<Integer>());

		model.addAttribute("isAdd", true);
		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/rarity-form.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "rarities");

		return "layout/main";
	}

	/*
	 * ========================= EDIT FORM =========================
	 */
	@GetMapping("/master/rarities/edit")
	public String editForm(@RequestParam int id, Model model) {

		Rarity rarity = rarityService.getRarityById(id);
		if (rarity == null) {
			return "redirect:/master/rarities";
		}

		model.addAttribute("rarity", rarity);
		model.addAttribute("categories", categoryService.getAllCategories());
		model.addAttribute("itemTypes", itemTypeService.getAllItemTypes());

		// rule mapping
		model.addAttribute("selectedCategoryRarityIds", rarityService.getCategoriesByRarity(id));
		model.addAttribute("selectedItemTypeRarityIds", rarityService.getItemTypesByRarity(id));

		model.addAttribute("isAdd", false);
		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/rarity-form.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "rarities");

		return "layout/main";
	}

	/*
	 * ========================= SAVE (INSERT / UPDATE + RULES)
	 * =========================
	 */
	@PostMapping("/master/rarities/save")
	public String save(@RequestParam int rarityId, @RequestParam String rarityName, @RequestParam String color,
			@RequestParam boolean isAdd, @RequestParam(required = false) List<Integer> categoryIds,
			@RequestParam(required = false) List<Integer> typeIds) {

		// ---------- save rarity ----------
		Rarity rarity = new Rarity();
		rarity.setRarityId(rarityId);
		rarity.setRarityName(rarityName);
		rarity.setColor(color);

		rarityService.saveRarity(rarity, isAdd);

		// ---------- save category rules ----------
		if (categoryIds == null) {
			categoryIds = new ArrayList<>();
		}
		rarityService.saveCategoryRarities(categoryIds, rarityId);

		// ---------- save item type rules ----------
		if (typeIds == null) {
			typeIds = new ArrayList<>();
		}
		rarityService.saveItemTypeRarities(typeIds, rarityId);

		return "redirect:/master/rarities";
	}

	/*
	 * ========================= DELETE =========================
	 */
	@PostMapping("/master/rarities/delete")
	public String delete(@RequestParam int id) {

		// ลบ rarity (rule จะถูกลบก่อนถ้าคุณ enforce FK)
		rarityService.deleteRarity(id);

		return "redirect:/master/rarities";
	}
}
