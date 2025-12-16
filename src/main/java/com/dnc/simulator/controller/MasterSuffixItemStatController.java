package com.dnc.simulator.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dnc.simulator.model.SuffixItem;
import com.dnc.simulator.model.SuffixItemAbility;
import com.dnc.simulator.model.SuffixItemAbilityStat;
import com.dnc.simulator.model.SuffixItemExtraStat;
import com.dnc.simulator.model.AbilityStatForm;
import com.dnc.simulator.model.Stat;
import com.dnc.simulator.model.SuffixItemExtraStatForm;
import com.dnc.simulator.service.StatService;
import com.dnc.simulator.service.SuffixItemService;
import com.dnc.simulator.service.SuffixItemStatService;

@Controller
public class MasterSuffixItemStatController {

	private final SuffixItemService suffixItemService;
	private final SuffixItemStatService suffixItemStatService;
	private final StatService statService;

	public MasterSuffixItemStatController(SuffixItemService suffixItemService,
			SuffixItemStatService suffixItemStatService, StatService statService) {

		this.suffixItemService = suffixItemService;
		this.suffixItemStatService = suffixItemStatService;
		this.statService = statService;
	}

	/*
	 * ===================================================== SHOW EXTRA STAT FORM
	 * (เพิ่ม logic default ตรงนี้เท่านั้น)
	 * =====================================================
	 */
	@GetMapping("/suffix-item/extra-stats/form")
	public String showExtraStatsForm(@RequestParam("suffixItemId") Integer suffixItemId, Model model) {

		if (suffixItemId == null || suffixItemId <= 0) {
			throw new IllegalArgumentException("Invalid suffixItemId");
		}

		// ===== (ของเดิม) โหลด suffix item =====
		SuffixItem suffixItem = suffixItemService.getById(suffixItemId.longValue());

		if (suffixItem == null) {
			throw new IllegalArgumentException("Suffix item not found: " + suffixItemId);
		}

		// ===== (ของเดิม) โหลด extra stats จาก DB =====
		List<SuffixItemExtraStat> extraStats = suffixItemStatService.getExtraStats(suffixItemId);

		// ===== (เพิ่มใหม่) ถ้าไม่มี → load default =====
		if (extraStats == null || extraStats.isEmpty()) {
			extraStats = suffixItemStatService.buildDefaultExtraStats(suffixItem.getSuffixTypeId());
		}

		// ===== (ของเดิม) master stat สำหรับ dropdown =====
		List<Stat> stats = statService.getAllStats();

		// ดึง ability ทั้งหมดของ suffix item นี้
		List<SuffixItemAbility> abilities = suffixItemStatService.getAbilitiesWithStatsBySuffixItemId(suffixItemId);

		// ใช้ ability แรก (กรณี 1 suffix item = 1 ability)
		SuffixItemAbility ability = abilities.isEmpty() ? new SuffixItemAbility() : abilities.get(0);

		model.addAttribute("suffixItemId", suffixItemId);
		model.addAttribute("itemId", suffixItem.getItemId());
		model.addAttribute("extraStats", extraStats);
		model.addAttribute("ability", ability);
		model.addAttribute("stats", stats);

		// 5. layout
		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/suffix-item-stats-form.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "suffix-items");

		return "layout/main";
	}

	/*
	 * ===================================================== SAVE EXTRA STATS
	 * (ไม่ยุ่ง delete เดิม / แค่ clear by suffixItemId)
	 * =====================================================
	 */
	@PostMapping("/suffix-item/extra-stats/save-form")
	public String saveExtraStats(@RequestParam("suffixItemId") Integer suffixItemId, SuffixItemExtraStatForm form) {

		if (suffixItemId == null || suffixItemId <= 0) {
			throw new IllegalArgumentException("Invalid suffixItemId");
		}

		// ===== (เพิ่มใหม่) clear extra stats เดิมของ suffix item นี้ =====
		suffixItemStatService.deleteExtraStatsBySuffixItemId(suffixItemId);

		// ===== (ของเดิม) save ใหม่จาก form =====
		if (form != null && form.getStats() != null) {

			for (SuffixItemExtraStat s : form.getStats()) {

				if (s.getStatId() == null) {
					continue;
				}

				SuffixItemExtraStat e = new SuffixItemExtraStat();
				e.setSuffixItemId(suffixItemId);
				e.setStatId(s.getStatId());
				e.setValueMin(s.getValueMin());
				e.setValueMax(s.getValueMax());
				e.setIsPercentage(s.getIsPercentage());

				suffixItemStatService.saveExtraStat(e);
			}
		}

		// ===== redirect กลับหน้าเดิม =====
		return "redirect:/suffix-item/extra-stats/form?suffixItemId=" + suffixItemId;
	}

	@PostMapping("/suffix-item/ability/save")
	public String saveSuffixItemAbility(@RequestParam("suffixItemId") Integer suffixItemId,
			@RequestParam(value = "abilityId", required = false) Integer abilityId,
			@RequestParam(value = "rawText", required = false) String rawText,
			@RequestParam(value = "type", required = false) String type, AbilityStatForm form) {

		if (suffixItemId == null || suffixItemId <= 0) {
			throw new IllegalArgumentException("Invalid suffixItemId");
		}

		/*
		 * ===================================================== 1) SAVE / UPDATE
		 * ABILITY (META) =====================================================
		 */
		SuffixItemAbility ability = new SuffixItemAbility();
		ability.setAbilityId(abilityId); // null = insert
		ability.setSuffixItemId(suffixItemId);
		ability.setRawText(rawText);
		ability.setType(type);

		Integer savedAbilityId = suffixItemStatService.saveAbility(ability);

		/*
		 * ===================================================== 2) CLEAR OLD ABILITY
		 * STATS =====================================================
		 */
		suffixItemStatService.deleteAbilityStatsByAbilityId(savedAbilityId);

		/*
		 * ===================================================== 3) SAVE ABILITY STATS
		 * (LIKE EXTRA STATS) =====================================================
		 */
		if (form != null && form.getStats() != null) {

			for (AbilityStatForm.StatForm s : form.getStats()) {

				if (s.getStatId() == null) {
					continue;
				}

				SuffixItemAbilityStat stat = new SuffixItemAbilityStat();
				stat.setAbilityId(savedAbilityId);
				stat.setStatId(s.getStatId());
				stat.setValueMin(s.getValueMin());
				stat.setValueMax(s.getValueMax());
				stat.setIsPercentage(s.getIsPercentage());

				suffixItemStatService.saveAbilityStat(stat);
			}
		}

		/*
		 * ===================================================== 4) REDIRECT BACK TO
		 * STATS PAGE =====================================================
		 */
		return "redirect:/suffix-item/extra-stats/form?suffixItemId=" + suffixItemId;
	}

}
