package com.dnc.simulator.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dnc.simulator.model.ItemType;
import com.dnc.simulator.service.ItemTypeService;

@Controller
public class MasterItemTypeController {

	private final ItemTypeService itemTypeService;

	public MasterItemTypeController(ItemTypeService itemTypeService) {
		this.itemTypeService = itemTypeService;
	}

	/*
	 * ========================= LIST =========================
	 */
	@GetMapping("/master/items")
	public String items(Model model) {

		List<ItemType> items = itemTypeService.getAllItemTypes();

		model.addAttribute("items", items);
		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/items.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "items");

		return "layout/main";
	}

	/*
	 * ========================= ADD FORM =========================
	 */
	@GetMapping("/master/items/add")
	public String addItemForm(Model model) {

		model.addAttribute("itemType", new ItemType());
		model.addAttribute("isAdd", true);

		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/item-form.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "items");

		return "layout/main";
	}

	/*
	 * ========================= EDIT FORM =========================
	 */
	@GetMapping("/master/items/edit")
	public String editItemForm(@RequestParam int id, Model model) {

		ItemType itemType = itemTypeService.getItemTypeById(id);
		if (itemType == null) {
			return "redirect:/master/items";
		}

		model.addAttribute("itemType", itemType);
		model.addAttribute("isAdd", false);

		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/item-form.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "items");

		return "layout/main";
	}

	/*
	 * ========================= SAVE (INSERT / UPDATE) =========================
	 */
	@PostMapping("/master/items/save")
	public String saveItem(@RequestParam int typeId, @RequestParam String typeName, @RequestParam String slot,
			@RequestParam int categoryId, @RequestParam boolean isAdd) {

		ItemType itemType = new ItemType();
		itemType.setTypeId(typeId);
		itemType.setTypeName(typeName);
		itemType.setSlot(slot);
		itemType.setCategoryId(categoryId);

		itemTypeService.saveItemType(itemType, isAdd);

		return "redirect:/master/items";
	}

	/*
	 * ========================= DELETE =========================
	 */
	@PostMapping("/master/items/delete")
	public String deleteItem(@RequestParam int id) {
		itemTypeService.deleteItemType(id);
		return "redirect:/master/items";
	}
}
