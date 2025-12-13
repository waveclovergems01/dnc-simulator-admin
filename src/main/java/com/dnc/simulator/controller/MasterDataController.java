package com.dnc.simulator.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MasterDataController {

	@GetMapping("/master/jobs")
	public String jobs(Model model) {
		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/jobs.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "jobs");
		return "layout/main";
	}

	@GetMapping("/master/items")
	public String items(Model model) {
		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/items.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "items");
		return "layout/main";
	}

	@GetMapping("/master/stats")
	public String stats(Model model) {
		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/stats.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "stats");
		return "layout/main";
	}

	@GetMapping("/master/rarity")
	public String rarity(Model model) {
		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/rarity.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "rarity");
		return "layout/main";
	}

	@GetMapping("/master/set-bonus")
	public String bonusItem(Model model) {
		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/set-bonus.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "set-bonus");
		return "layout/main";
	}
}
