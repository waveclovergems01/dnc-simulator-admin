package com.dnc.simulator.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

	@GetMapping("/")
	public String root() {
		return "redirect:/dashboard";
	}

	@GetMapping("/dashboard")
	public String dashboard(Model model) {
		model.addAttribute("contentPage", "/WEB-INF/views/pages/dashboard.jsp");
		model.addAttribute("activeMenu", "dashboard");
		return "layout/main";
	}

	@GetMapping("/profile")
	public String profile(Model model) {
		model.addAttribute("contentPage", "/WEB-INF/views/pages/profile.jsp");
		model.addAttribute("activeMenu", "profile");
		return "layout/main";
	}

	@GetMapping("/users")
	public String users(Model model) {
		model.addAttribute("contentPage", "/WEB-INF/views/pages/users.jsp");
		model.addAttribute("activeMenu", "users");
		return "layout/main";
	}
}
