package com.dnc.simulator.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dnc.simulator.model.EquipmentItem;
import com.dnc.simulator.model.SuffixItem;
import com.dnc.simulator.model.SuffixItemAbility;
import com.dnc.simulator.model.SuffixItemAbilityStat;
import com.dnc.simulator.model.SuffixItemExtraStat;
import com.dnc.simulator.model.SuffixType;
import com.dnc.simulator.service.EquipmentItemService;
import com.dnc.simulator.service.SuffixCloneService;
import com.dnc.simulator.service.SuffixItemService;
import com.dnc.simulator.service.SuffixService;

@Controller
@RequestMapping("/master/suffix-items")
public class SuffixCloneEquipmentController {

	private final EquipmentItemService equipmentItemService;
	private final SuffixItemService suffixItemService;
	private final SuffixCloneService suffixCloneService;
	private final EquipmentItemService equipmentService;
	private final SuffixService suffixService;

	public SuffixCloneEquipmentController(EquipmentItemService equipmentItemService,
			SuffixItemService suffixItemService, SuffixCloneService suffixCloneService,
			EquipmentItemService equipmentService, SuffixService suffixService) {

		this.equipmentItemService = equipmentItemService;
		this.suffixItemService = suffixItemService;
		this.suffixCloneService = suffixCloneService;
		this.equipmentService = equipmentService;
		this.suffixService = suffixService;
	}

	/*
	 * ========================================================= CLONE SUFFIX –
	 * SELECT EQUIPMENT =========================================================
	 */
	@GetMapping("/clone-suffix")
	public String clonePage(@RequestParam(required = false) Long equipmentItemId, Model model) {

		/*
		 * ===================================================== 1. BASE: LOAD ALL
		 * EQUIPMENT =====================================================
		 */
		List<EquipmentItem> allEquipments = equipmentItemService.getAll();

		List<EquipmentItem> sourceEquipments = new java.util.ArrayList<>();
		List<EquipmentItem> targetEquipments = new java.util.ArrayList<>();

		/*
		 * ===================================================== 2. SPLIT: CHECK EACH
		 * EQUIPMENT HAS SUFFIX OR NOT
		 * =====================================================
		 */
		for (EquipmentItem e : allEquipments) {

			List<SuffixItem> suffixList = suffixItemService.getByItemId(e.getItemId());

			if (suffixList != null && !suffixList.isEmpty()) {
				// ✅ equipment ที่มี suffix
				sourceEquipments.add(e);
			} else {
				// ✅ equipment ที่ยังไม่มี suffix
				targetEquipments.add(e);
			}
		}

		/*
		 * ===================================================== 3. SEND DROPDOWN DATA
		 * TO JSP =====================================================
		 */
		model.addAttribute("sourceEquipments", sourceEquipments);
		model.addAttribute("targetEquipments", targetEquipments);

		/*
		 * ===================================================== 4. LOAD SUFFIX WHEN
		 * CLICK "LOAD" =====================================================
		 */
		if (equipmentItemId != null) {

			EquipmentItem item = equipmentItemService.getById(equipmentItemId);

			if (item != null) {

				List<SuffixItem> suffixes = suffixItemService.getByItemId(equipmentItemId);

				model.addAttribute("equipmentItem", item);
				model.addAttribute("suffixes", suffixes);
			}
		}

		/*
		 * ===================================================== 5. RENDER PAGE
		 * =====================================================
		 */
		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/suffix-clone-suffix.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "suffix-items");

		return "layout/main";
	}

	/*
	 * ========================================================= CLONE SUFFIX – SAVE
	 * =========================================================
	 */
	@PostMapping("/clone-suffix/save")
	public String cloneSave(

			@RequestParam Long originalEquipmentItemId, @RequestParam Long newEquipmentItemId,
			@RequestParam("suffixItemId") List<Long> suffixItemIds) {

		Map<Long, SuffixItem> clones = new LinkedHashMap<>();

		for (Long suffixItemId : suffixItemIds) {

			/*
			 * ===================================================== 1. LOAD ORIGINAL SUFFIX
			 * (ต้อง join ability + stats มาแล้ว)
			 * =====================================================
			 */
			SuffixItem original = suffixItemService.getById(suffixItemId);
			if (original == null) {
				continue;
			}

			EquipmentItem newEquipmentItem = equipmentService.getById(newEquipmentItemId);
			SuffixType newSuffixType = suffixService.getSuffixTypeById(original.getSuffixTypeId());

			/*
			 * ===================================================== 2. CLONE SUFFIX ITEM
			 * =====================================================
			 */
			SuffixItem clone = new SuffixItem();
			clone.setId(null);
			clone.setItemId(newEquipmentItemId);
			clone.setName(newEquipmentItem.getName() + " (" + newSuffixType.getSuffixName() + ")");
			clone.setSuffixTypeId(original.getSuffixTypeId());

			/*
			 * ===================================================== 3. CLONE EXTRA STATS
			 * =====================================================
			 */
			if (original.getExtraStats() != null && !original.getExtraStats().isEmpty()) {

				List<SuffixItemExtraStat> clonedExtraStats = new java.util.ArrayList<>();

				for (SuffixItemExtraStat es : original.getExtraStats()) {

					SuffixItemExtraStat esClone = new SuffixItemExtraStat();
					esClone.setId(null);
					esClone.setSuffixItemId(null); // set หลัง insert suffix
					esClone.setStatId(es.getStatId());
					esClone.setValueMin(es.getValueMin());
					esClone.setValueMax(es.getValueMax());
					esClone.setIsPercentage(es.getIsPercentage());

					clonedExtraStats.add(esClone);
				}

				clone.setExtraStats(clonedExtraStats);
			}

			/*
			 * ===================================================== 4. CLONE ABILITIES +
			 * ABILITY STATS =====================================================
			 */
			if (original.getAbilities() != null && !original.getAbilities().isEmpty()) {

				List<SuffixItemAbility> clonedAbilities = new java.util.ArrayList<>();

				for (SuffixItemAbility ab : original.getAbilities()) {

					SuffixItemAbility abClone = new SuffixItemAbility();
					abClone.setAbilityId(null);
					abClone.setSuffixItemId(null); // set หลัง insert suffix
					abClone.setRawText(ab.getRawText());
					abClone.setType(ab.getType());

					/* -------- ABILITY STATS -------- */
					if (ab.getAbilityStats() != null && !ab.getAbilityStats().isEmpty()) {

						List<SuffixItemAbilityStat> clonedStats = new java.util.ArrayList<>();

						for (SuffixItemAbilityStat st : ab.getAbilityStats()) {

							SuffixItemAbilityStat stClone = new SuffixItemAbilityStat();
							stClone.setId(null);
							stClone.setAbilityId(null); // set หลัง insert ability
							stClone.setStatId(st.getStatId());
							stClone.setValueMin(st.getValueMin());
							stClone.setValueMax(st.getValueMax());
							stClone.setIsPercentage(st.getIsPercentage());

							clonedStats.add(stClone);
						}

						abClone.setAbilityStats(clonedStats);
					}

					clonedAbilities.add(abClone);
				}

				clone.setAbilities(clonedAbilities);
			}

			/*
			 * ===================================================== 5. ADD TO MAP
			 * =====================================================
			 */
			clones.put(suffixItemId, clone);
		}

		/*
		 * ========================================================= 6. SAVE ALL
		 * (TRANSACTION) =========================================================
		 */
		int success = suffixCloneService.cloneEquipment(clones);

		return "redirect:/master/suffix-items?cloned=" + success;
	}

}
