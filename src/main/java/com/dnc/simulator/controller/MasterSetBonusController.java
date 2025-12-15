package com.dnc.simulator.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.dnc.simulator.model.SetBonus;
import com.dnc.simulator.service.SetBonusService;
import com.dnc.simulator.service.StatService;

@Controller
public class MasterSetBonusController {

	private final SetBonusService setBonusService;
	private final StatService statService;

	public MasterSetBonusController(SetBonusService setBonusService, StatService statService) {
		this.setBonusService = setBonusService;
		this.statService = statService;
	}

	/* ========================= LIST ========================= */
	@GetMapping("/master/set-bonus")
	public String list(Model model) {

		model.addAttribute("sets", setBonusService.getAll());

		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/set-bonus.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "setBonus");

		return "layout/main";
	}

	/* ========================= ADD ========================= */
	@GetMapping("/master/set-bonus/add")
	public String addForm(Model model) {

		model.addAttribute("setBonus", new SetBonus());
		model.addAttribute("stats", statService.getAllStats()); // ⭐ สำคัญ
		model.addAttribute("isAdd", true);

		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/set-bonus-form.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "setBonus");

		return "layout/main";
	}

	/* ========================= EDIT ========================= */
	@GetMapping("/master/set-bonus/edit")
	public String editForm(@RequestParam Integer setId, Model model) {

		SetBonus setBonus = setBonusService.getById(setId);
		if (setBonus == null) {
			return "redirect:/master/set-bonus";
		}

		model.addAttribute("setBonus", setBonus);
		model.addAttribute("stats", statService.getAllStats()); // ⭐ สำคัญ
		model.addAttribute("isAdd", false);

		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/set-bonus-form.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "setBonus");

		return "layout/main";
	}

	/* ========================= CLONE ========================= */
	@GetMapping("/master/set-bonus/clone")
	public String cloneForm(@RequestParam Integer setId, Model model) {

		SetBonus original = setBonusService.getById(setId);
		if (original == null) {
			return "redirect:/master/set-bonus";
		}

		// reset id
		original.setSetId(null);

		model.addAttribute("setBonus", original);
		model.addAttribute("stats", statService.getAllStats()); // ⭐ สำคัญ
		model.addAttribute("isAdd", true);

		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/set-bonus-form.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "setBonus");

		return "layout/main";
	}

	/* ========================= SAVE ========================= */
	@PostMapping("/master/set-bonus/save")
	public String save(@ModelAttribute SetBonus setBonus, @RequestParam boolean isAdd) {

		setBonusService.save(setBonus);
		return "redirect:/master/set-bonus";
	}

	/* ========================= DELETE ========================= */
	@PostMapping("/master/set-bonus/delete")
	public String delete(@RequestParam Integer setId) {

		setBonusService.delete(setId);
		return "redirect:/master/set-bonus";
	}
}
