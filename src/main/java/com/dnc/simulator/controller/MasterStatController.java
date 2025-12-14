package com.dnc.simulator.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dnc.simulator.model.Stat;
import com.dnc.simulator.service.StatService;

@Controller
public class MasterStatController {

	private final StatService statService;

	public MasterStatController(StatService statService) {
		this.statService = statService;
	}

	/*
	 * ========================= LIST =========================
	 */
	@GetMapping("/master/stats")
	public String stats(Model model) {

		List<Stat> stats = statService.getAllStats();

		model.addAttribute("stats", stats);
		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/stats.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "stats");

		return "layout/main";
	}

	/*
	 * ========================= ADD FORM =========================
	 */
	@GetMapping("/master/stats/add")
	public String addStatForm(Model model) {

		model.addAttribute("stat", new Stat());
		model.addAttribute("isAdd", true);

		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/stat-form.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "stats");

		return "layout/main";
	}

	/*
	 * ========================= EDIT FORM =========================
	 */
	@GetMapping("/master/stats/edit")
	public String editStatForm(@RequestParam int id, Model model) {

		Stat stat = statService.getStatById(id);
		if (stat == null) {
			return "redirect:/master/stats";
		}

		model.addAttribute("stat", stat);
		model.addAttribute("isAdd", false);

		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/stat-form.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "stats");

		return "layout/main";
	}

	/*
	 * ========================= SAVE (INSERT / UPDATE) =========================
	 */
	@PostMapping("/master/stats/save")
	public String saveStat(@RequestParam int statId, @RequestParam String statName, @RequestParam String displayName,
			@RequestParam int statCatId, @RequestParam String statCatName, @RequestParam boolean isPercentage,
			@RequestParam boolean isAdd) {

		Stat stat = new Stat();
		stat.setStatId(statId);
		stat.setStatName(statName);
		stat.setDisplayName(displayName);
		stat.setStatCatId(statCatId);
		stat.setStatCatName(statCatName);
		stat.setIsPercentage(isPercentage);

		statService.saveStat(stat, isAdd);

		return "redirect:/master/stats";
	}

	/*
	 * ========================= DELETE =========================
	 */
	@PostMapping("/master/stats/delete")
	public String deleteStat(@RequestParam int id) {
		statService.deleteStat(id);
		return "redirect:/master/stats";
	}
}
