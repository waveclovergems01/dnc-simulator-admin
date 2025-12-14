package com.dnc.simulator.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dnc.simulator.model.SuffixGroup;
import com.dnc.simulator.model.SuffixGroupItem;
import com.dnc.simulator.model.SuffixType;
import com.dnc.simulator.service.ItemTypeService;
import com.dnc.simulator.service.SuffixService;

@Controller
public class MasterSuffixController {

	private final SuffixService suffixService;
	private final ItemTypeService itemTypeService;

	public MasterSuffixController(SuffixService suffixService, ItemTypeService itemTypeService) {
		this.suffixService = suffixService;
		this.itemTypeService = itemTypeService;
	}

	/* ========================= LIST ========================= */
	@GetMapping("/master/suffix")
	public String suffixHome(Model model) {

		model.addAttribute("suffixTypes", suffixService.getAllSuffixTypes());
		model.addAttribute("groups", suffixService.getAllGroups());
		model.addAttribute("itemTypes", itemTypeService.getAllItemTypes());

		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/suffix.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "suffix");

		return "layout/main";
	}

	/* ========================= SUFFIX TYPE ========================= */
	@GetMapping("/master/suffix/type/add")
	public String addSuffixTypeForm(Model model) {

		model.addAttribute("suffixType", new SuffixType());
		model.addAttribute("isAdd", true);

		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/suffix-type-form.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "suffix");

		return "layout/main";
	}

	@GetMapping("/master/suffix/type/edit")
	public String editSuffixTypeForm(@RequestParam Integer suffixId, Model model) {

		model.addAttribute("suffixType", suffixService.getSuffixTypeById(suffixId));
		model.addAttribute("isAdd", false);

		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/suffix-type-form.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "suffix");

		return "layout/main";
	}

	@PostMapping("/master/suffix/type/save")
	public String saveSuffixType(@RequestParam Integer suffixId, @RequestParam String suffixName,
			@RequestParam boolean isAdd) {

		SuffixType st = new SuffixType();
		st.setSuffixId(suffixId);
		st.setSuffixName(suffixName);

		if (isAdd) {
			suffixService.createSuffixType(st);
		} else {
			suffixService.updateSuffixType(st);
		}
		return "redirect:/master/suffix";
	}

	@PostMapping("/master/suffix/type/delete")
	public String deleteSuffixType(@RequestParam Integer suffixId) {
		suffixService.deleteSuffixType(suffixId);
		return "redirect:/master/suffix";
	}

	/* ========================= SUFFIX GROUP ========================= */
	@GetMapping("/master/suffix/group/add")
	public String addGroupForm(Model model) {

		model.addAttribute("group", new SuffixGroup());
		model.addAttribute("itemTypes", itemTypeService.getAllItemTypes());
		model.addAttribute("isAdd", true);

		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/suffix-group-form.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "suffix");

		return "layout/main";
	}

	@GetMapping("/master/suffix/group/edit")
	public String editGroupForm(@RequestParam Integer groupId, Model model) {

		model.addAttribute("group", suffixService.getGroupById(groupId));
		model.addAttribute("itemTypes", itemTypeService.getAllItemTypes());
		model.addAttribute("isAdd", false);

		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/suffix-group-form.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "suffix");

		return "layout/main";
	}

	@PostMapping("/master/suffix/group/save")
	public String saveGroup(@RequestParam Integer groupId, @RequestParam Integer itemTypeId,
			@RequestParam boolean isAdd) {

		SuffixGroup g = new SuffixGroup();
		g.setGroupId(groupId);
		g.setItemTypeId(itemTypeId);

		if (isAdd) {
			suffixService.createGroup(g);
		} else {
			suffixService.updateGroup(g);
		}
		return "redirect:/master/suffix";
	}

	@PostMapping("/master/suffix/group/delete")
	public String deleteGroup(@RequestParam Integer groupId) {
		suffixService.deleteGroup(groupId);
		return "redirect:/master/suffix";
	}

	/* ========================= SUFFIX GROUP ITEMS ========================= */
	@GetMapping("/master/suffix/group/items")
	public String groupItems(@RequestParam Integer groupId, Model model) {

		SuffixGroup group = suffixService.getGroupById(groupId);
		if (group == null) {
			return "redirect:/master/suffix";
		}

		model.addAttribute("group", group);
		model.addAttribute("items", suffixService.getGroupItemsByGroupId(groupId));
		model.addAttribute("suffixTypes", suffixService.getAllSuffixTypes());

		// 👉 สำคัญ: ใช้แสดง Item Type Name ด้านบนหน้า
		model.addAttribute("itemTypes", itemTypeService.getAllItemTypes());

		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/suffix-group-items.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "suffix");

		return "layout/main";
	}

	/* ========================= SAVE (ADD / EDIT ITEM) ========================= */
	@PostMapping("/master/suffix/group/item/save")
	public String saveGroupItem(@RequestParam(required = false) Integer id, @RequestParam Integer groupId,
			@RequestParam Integer suffixId, @RequestParam String mode,
			@RequestParam(required = false, defaultValue = "true") boolean isAdd) {

		SuffixGroupItem item = new SuffixGroupItem();
		item.setId(id);
		item.setGroupId(groupId);
		item.setSuffixId(suffixId);
		item.setMode(mode);

		if (isAdd) {
			suffixService.createGroupItem(item);
		} else {
			suffixService.updateGroupItem(item);
		}

		return "redirect:/master/suffix/group/items?groupId=" + groupId;
	}

	/* ========================= DELETE ITEM ========================= */
	@PostMapping("/master/suffix/group/item/delete")
	public String deleteGroupItem(@RequestParam Integer id, @RequestParam Integer groupId) {

		suffixService.deleteGroupItem(id);
		return "redirect:/master/suffix/group/items?groupId=" + groupId;
	}
}
