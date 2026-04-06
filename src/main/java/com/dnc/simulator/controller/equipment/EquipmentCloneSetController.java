package com.dnc.simulator.controller.equipment;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dnc.simulator.model.EquipmentItemStat;
import com.dnc.simulator.model.equipment.EquipmentItem;
import com.dnc.simulator.service.ItemTypeService;
import com.dnc.simulator.service.JobService;
import com.dnc.simulator.service.RarityService;
import com.dnc.simulator.service.SetBonusService;
import com.dnc.simulator.service.equipment.EquipmentCloneService;
import com.dnc.simulator.service.equipment.EquipmentItemService;

@Controller
@RequestMapping("/master/equipment")
public class EquipmentCloneSetController {

	private final EquipmentItemService equipmentItemService;
	private final EquipmentCloneService equipmentCloneService;
	private final JobService jobService;
	private final RarityService rarityService;
	private final SetBonusService setBonusService;
	private final ItemTypeService itemTypeService;

	public EquipmentCloneSetController(
			EquipmentItemService equipmentItemService,
			EquipmentCloneService equipmentCloneService,
			JobService jobService,
			RarityService rarityService,
			SetBonusService setBonusService,
			ItemTypeService itemTypeService) {

		this.equipmentItemService = equipmentItemService;
		this.equipmentCloneService = equipmentCloneService;
		this.jobService = jobService;
		this.rarityService = rarityService;
		this.setBonusService = setBonusService;
		this.itemTypeService = itemTypeService;
	}

	@GetMapping("/clone-set")
	public String cloneSetPage(
			@RequestParam(value = "setId", required = false) Integer setId,
			Model model) {

		List<EquipmentItem> allItems = equipmentItemService.getAll();
		Set<Integer> setIds = new LinkedHashSet<Integer>();

		for (EquipmentItem e : allItems) {
			if (e.getSetId() != null) {
				setIds.add(e.getSetId());
			}
		}

		model.addAttribute("setIds", setIds);
		model.addAttribute("setBonuses", setBonusService.getAll());
		model.addAttribute("jobs", jobService.getAllJobs());
		model.addAttribute("rarities", rarityService.getAllRarities());
		model.addAttribute("itemTypes", itemTypeService.getAllItemTypes());

		if (setId != null) {
			List<EquipmentItem> originals = equipmentCloneService.getItemsBySetId(Long.valueOf(setId));
			model.addAttribute("setId", setId);
			model.addAttribute("originals", originals);
		}

		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/equipment/equipment-clone-set.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "equipment");

		return "layout/main";
	}

	@PostMapping("/clone-set/save")
	public String cloneSetSave(
			@RequestParam("newSetId") String newSetIdStr,
			@RequestParam(value = "globalJob", required = false) String globalJobStr,
			@RequestParam(value = "globalRarity", required = false) String globalRarityStr,
			@RequestParam(value = "globalReqLv", required = false) String globalReqLvStr,
			@RequestParam(value = "globalType", required = false) String globalTypeStr,
			@RequestParam("originalItemId") List<Long> originalItemIds,
			@RequestParam("newItemId") List<String> newItemIdStrs,
			@RequestParam Map<String, String> params) {

		Integer newSetId = parseInt(newSetIdStr, null);
		Integer globalJob = parseInt(globalJobStr, null);
		Integer globalRarity = parseInt(globalRarityStr, null);
		Integer globalReqLv = parseInt(globalReqLvStr, null);
		Integer globalType = parseInt(globalTypeStr, null);

		Map<Long, EquipmentItem> clones = new LinkedHashMap<>();

		for (int i = 0; i < originalItemIds.size(); i++) {

			Long originalId = originalItemIds.get(i);
			Long newItemId = parseLong(getListValue(newItemIdStrs, i), null);

			if (newItemId == null) {
				continue;
			}

			EquipmentItem original = null;

			if (originalId != null && originalId.longValue() > 0L) {
				original = equipmentItemService.getById(originalId);
				if (original == null) {
					continue;
				}
			}

			String suffix = (originalId != null && originalId.longValue() > 0L)
					? String.valueOf(originalId)
					: "n" + i;

			EquipmentItem clone = new EquipmentItem();
			clone.setItemId(newItemId);

			String name = safeTrim(params.get("name_" + suffix));
			if (name.isEmpty()) {
				continue;
			}
			clone.setName(name);

			Integer typeId = globalType != null
					? globalType
					: parseInt(params.get("typeId_" + suffix), original != null ? original.getTypeId() : null);

			if (typeId == null) {
				continue;
			}
			clone.setTypeId(typeId);

			Integer jobId = globalJob != null
					? globalJob
					: parseInt(params.get("jobId_" + suffix), original != null ? original.getJobId() : null);
			clone.setJobId(jobId);

			Integer rarityId = globalRarity != null
					? globalRarity
					: parseInt(params.get("rarityId_" + suffix), original != null ? original.getRarityId() : null);
			clone.setRarityId(rarityId);

			Integer reqLv = globalReqLv != null
					? globalReqLv
					: parseInt(params.get("requiredLevel_" + suffix),
							original != null ? original.getRequiredLevel() : null);
			clone.setRequiredLevel(reqLv);

			clone.setDurability(original != null && original.getDurability() != null ? original.getDurability() : 0);
			clone.setSetId(newSetId);

			clone.setIconBlob(original != null ? original.getIconBlob() : null);
			clone.setIconMime(original != null ? original.getIconMime() : null);
			clone.setIconName(original != null ? original.getIconName() : null);

			if (original != null && original.getStats() != null) {
				List<EquipmentItemStat> newStats = new ArrayList<EquipmentItemStat>();

				for (EquipmentItemStat s : original.getStats()) {
					EquipmentItemStat x = new EquipmentItemStat();
					x.setItemId(null);
					x.setStatId(s.getStatId());
					x.setValueMin(s.getValueMin());
					x.setValueMax(s.getValueMax());
					x.setIsPercentage(s.getIsPercentage());
					newStats.add(x);
				}

				clone.setStats(newStats);
			}

			clones.put(newItemId, clone);
		}

		int success = equipmentCloneService.cloneSetWithEditedValues(null, clones);
		return "redirect:/master/equipment?cloned=" + success;
	}

	private String getListValue(List<String> list, int index) {
		if (list == null) {
			return null;
		}
		if (index < 0 || index >= list.size()) {
			return null;
		}
		return list.get(index);
	}

	private String safeTrim(String value) {
		return value == null ? "" : value.trim();
	}

	private Integer parseInt(String value, Integer def) {
		try {
			return (value != null && !value.trim().isEmpty()) ? Integer.valueOf(value.trim()) : def;
		} catch (Exception e) {
			return def;
		}
	}

	private Long parseLong(String value, Long def) {
		try {
			return (value != null && !value.trim().isEmpty()) ? Long.valueOf(value.trim()) : def;
		} catch (Exception e) {
			return def;
		}
	}
}