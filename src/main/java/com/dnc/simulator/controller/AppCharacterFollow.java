package com.dnc.simulator.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/app")
public class AppCharacterFollow {

	@GetMapping("/charfollow")
	public String exportJson(Model model) {
		model.addAttribute("contentPage", "/WEB-INF/views/pages/app/charfollow.jsp");
		model.addAttribute("activeMenuGroup", "app");
		model.addAttribute("activeMenu", "charfollow");

		return "layout/main";
	}
}
