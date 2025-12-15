package com.dnc.simulator.controller;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.dnc.simulator.model.EquipmentItem;
import com.dnc.simulator.model.EquipmentItemStat;
import com.dnc.simulator.service.EquipmentCloneService;
import com.dnc.simulator.service.EquipmentItemService;
import com.dnc.simulator.service.JobService;
import com.dnc.simulator.service.RarityService;
import com.dnc.simulator.service.SetBonusService;

@Controller
@RequestMapping("/master/equipment")
public class EquipmentCloneSetController {

	private final EquipmentItemService equipmentItemService;
	private final EquipmentCloneService equipmentCloneService;
	private final JobService jobService;
	private final RarityService rarityService;
	private final SetBonusService setBonusService;

	public EquipmentCloneSetController(EquipmentItemService equipmentItemService,
			EquipmentCloneService equipmentCloneService, JobService jobService, RarityService rarityService,
			SetBonusService setBonusService) {

		this.equipmentItemService = equipmentItemService;
		this.equipmentCloneService = equipmentCloneService;
		this.jobService = jobService;
		this.rarityService = rarityService;
		this.setBonusService = setBonusService;
	}

	/*
	 * ========================================================= CLONE SET – SELECT
	 * ORIGINAL SET + PREVIEW
	 * =========================================================
	 */
	@GetMapping("/clone-set")
	public String cloneSetPage(@RequestParam(required = false) Integer setId, Model model) {

		// fallback setIds
		List<EquipmentItem> allItems = equipmentItemService.getAll();
		Set<Integer> setIds = new LinkedHashSet<>();
		for (EquipmentItem e : allItems) {
			if (e.getSetId() != null) {
				setIds.add(e.getSetId());
			}
		}

		model.addAttribute("setIds", setIds);
		model.addAttribute("setBonuses", setBonusService.getAll());
		model.addAttribute("jobs", jobService.getAllJobs());
		model.addAttribute("rarities", rarityService.getAllRarities());

		if (setId != null) {
			List<EquipmentItem> originals = equipmentCloneService.getItemsBySetId(Long.valueOf(setId));
			model.addAttribute("setId", setId);
			model.addAttribute("originals", originals);
		}

		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/equipment-clone-set.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "equipment");

		return "layout/main";
	}

	/*
	 * ========================================================= CLONE SET – SAVE
	 * (GLOBAL VALUES) =========================================================
	 */
	@PostMapping("/clone-set/save")
	public String cloneSetSave(

			@RequestParam Integer newSetId,

			@RequestParam(required = false) Integer globalJob, @RequestParam(required = false) Integer globalRarity,
			@RequestParam(required = false) Integer globalReqLv,

			@RequestParam("originalItemId") List<Long> originalItemIds,
			@RequestParam("newItemId") List<Long> newItemIds,

			@RequestParam Map<String, String> params) {

		Map<Long, EquipmentItem> clones = new LinkedHashMap<>();

		for (int i = 0; i < originalItemIds.size(); i++) {

			Long originalId = originalItemIds.get(i);
			Long newItemId = newItemIds.get(i);

			if (newItemId == null)
				continue;

			EquipmentItem original = equipmentItemService.getById(originalId);
			if (original == null)
				continue;

			EquipmentItem clone = new EquipmentItem();
			clone.setItemId(newItemId);

			// name already processed by UI
			clone.setName(params.get("name_" + originalId));

			clone.setTypeId(original.getTypeId());

			clone.setJobId(
					globalJob != null ? globalJob : parseInt(params.get("jobId_" + originalId), original.getJobId()));

			clone.setRarityId(globalRarity != null ? globalRarity
					: parseInt(params.get("rarityId_" + originalId), original.getRarityId()));

			clone.setRequiredLevel(globalReqLv != null ? globalReqLv
					: parseInt(params.get("requiredLevel_" + originalId), original.getRequiredLevel()));

			clone.setDurability(parseInt(params.get("durability_" + originalId), original.getDurability()));

			// global set id
			clone.setSetId(newSetId);

			// deep clone stats
			if (original.getStats() != null) {
				for (EquipmentItemStat s : original.getStats()) {
					s.setItemId(null);
				}
				clone.setStats(original.getStats());
			}

			clones.put(originalId, clone);
		}

		int success = equipmentCloneService.cloneSetWithEditedValues(null, clones);

		return "redirect:/master/equipment?cloned=" + success;
	}

	/*
	 * ========================================================= utils
	 * =========================================================
	 */
	private Integer parseInt(String v, Integer def) {
		try {
			return (v != null && !v.isEmpty()) ? Integer.valueOf(v) : def;
		} catch (Exception e) {
			return def;
		}
	}
}
