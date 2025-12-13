package com.dnc.simulator.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dnc.simulator.model.Job;
import com.dnc.simulator.service.JobService;

@Controller
public class MasterDataController {

	private final JobService jobService;

	public MasterDataController(JobService jobService) {
		this.jobService = jobService;
	}

	/*
	 * ========================= LIST =========================
	 */
	@GetMapping("/master/jobs")
	public String jobs(Model model) {

		List<Job> jobs = jobService.getAllJobs();

		model.addAttribute("jobs", jobs);
		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/jobs.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "jobs");

		return "layout/main";
	}

	/*
	 * ========================= ADD FORM =========================
	 */
	@GetMapping("/master/jobs/add")
	public String addJobForm(Model model) {

		model.addAttribute("job", new Job());
		model.addAttribute("allJobs", jobService.getAllJobs());

		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/job-form.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "jobs");

		return "layout/main";
	}

	/*
	 * ========================= SAVE (INSERT) =========================
	 */
	@PostMapping("/master/jobs/save")
	public String saveJob(@RequestParam Integer id, @RequestParam String name, @RequestParam int classId,
			@RequestParam String className, @RequestParam int inherit, @RequestParam int requiredLevel,
			@RequestParam(required = false) List<Integer> nextClassIds) {

		Job job = new Job();
		job.setId(id);
		job.setName(name);
		job.setClassId(classId);
		job.setClassName(className);
		job.setInherit(inherit);
		job.setRequiredLevel(requiredLevel);

		if (nextClassIds != null) {
			job.setNextClassIds(nextClassIds);
		}

		jobService.insert(job);
		return "redirect:/master/jobs";
	}

	/*
	 * ========================= DELETE =========================
	 */
	@PostMapping("/master/jobs/delete")
	public String deleteJob(@RequestParam int id) {
		jobService.delete(id);
		return "redirect:/master/jobs";
	}

	/*
	 * ========================================================= OTHER MASTER PAGES
	 * (STATIC FOR NOW) =========================================================
	 */

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
