package com.dnc.simulator.controller.masterdata;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dnc.simulator.service.patch.PatchLevelService;

@Controller
@RequestMapping("/master/patch")
public class PatchController {

	private final PatchLevelService patchLevelService;

	public PatchController(PatchLevelService patchLevelService) {
		this.patchLevelService = patchLevelService;
	}

	@GetMapping("/viewPatchLevel")
	public String view(Model model) {

		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/patch/viewPatchLevel.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "patch");

		model.addAttribute("levelList", patchLevelService.findAll());

		return "layout/main";
	}

	@GetMapping("/addPatchLevel")
	public String add(Model model) {

		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/patch/addPatchLevel.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "patch");

		model.addAttribute("mode", "ADD");

		return "layout/main";
	}

	@GetMapping("/editPatchLevel")
	public String edit(@RequestParam("id") Long id, Model model) {

		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/patch/addPatchLevel.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "patch");

		model.addAttribute("mode", "EDIT");
		model.addAttribute("level", patchLevelService.findById(id));

		return "layout/main";
	}

	@PostMapping("/savePatchLevel")
	@ResponseBody
	public ResponseEntity<?> save(@RequestParam(value = "id", required = false) Long id,
			@RequestParam("level") String levelStr) {

		try {
			levelStr = levelStr == null ? "" : levelStr.trim();

			if (levelStr.isEmpty()) {
				return ResponseEntity.badRequest().body("Level is required");
			}

			if (!levelStr.matches("^\\d+$")) {
				return ResponseEntity.badRequest().body("Level must be number only");
			}

			Integer level = Integer.valueOf(levelStr);
			if (level <= 0) {
				return ResponseEntity.badRequest().body("Level must be greater than 0");
			}

			if (id == null) {
				patchLevelService.create(level);
			} else {
				patchLevelService.update(id, level);
			}

			return ResponseEntity.ok("SUCCESS");

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(e.getMessage() == null ? "ERROR" : e.getMessage());
		}
	}

	@PostMapping("/deletePatchLevel")
	public String delete(@RequestParam("id") Long id) {
		patchLevelService.delete(id);
		return "redirect:/master/patch/viewPatchLevel";
	}
}