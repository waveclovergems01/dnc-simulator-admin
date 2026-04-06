package com.dnc.simulator.controller.equipment;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.dnc.simulator.model.EquipmentItemStat;
import com.dnc.simulator.model.equipment.EquipmentItem;
import com.dnc.simulator.service.ItemTypeService;
import com.dnc.simulator.service.JobService;
import com.dnc.simulator.service.RarityService;
import com.dnc.simulator.service.SetBonusService;
import com.dnc.simulator.service.StatService;
import com.dnc.simulator.service.equipment.EquipmentItemService;

@Controller
@RequestMapping("/master/equipment")
public class MasterEquipmentItemController {

	private final EquipmentItemService equipmentService;
	private final ItemTypeService itemTypeService;
	private final JobService jobService;
	private final RarityService rarityService;
	private final StatService statService;
	private final SetBonusService setBonusService;

	public MasterEquipmentItemController(
			EquipmentItemService equipmentService,
			ItemTypeService itemTypeService,
			JobService jobService,
			RarityService rarityService,
			StatService statService,
			SetBonusService setBonusService) {

		this.equipmentService = equipmentService;
		this.itemTypeService = itemTypeService;
		this.jobService = jobService;
		this.rarityService = rarityService;
		this.statService = statService;
		this.setBonusService = setBonusService;
	}

	@GetMapping("")
	public String list(Model model) {

		List<EquipmentItem> items = equipmentService.getAll();
		model.addAttribute("items", items);

		model.addAttribute("itemTypes", itemTypeService.getAllItemTypes());
		model.addAttribute("jobs", jobService.getAllJobs());
		model.addAttribute("rarities", rarityService.getAllRarities());
		model.addAttribute("setBonuses", setBonusService.getAll());

		Set<Integer> setIds = new LinkedHashSet<Integer>();
		for (EquipmentItem e : items) {
			if (e.getSetId() != null) {
				setIds.add(e.getSetId());
			}
		}
		model.addAttribute("setIds", setIds);

		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/equipment/equipment.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "equipment");

		return "layout/main";
	}

	@GetMapping("/add")
	public String addForm(Model model) {

		model.addAttribute("item", new EquipmentItem());
		model.addAttribute("itemTypes", itemTypeService.getAllItemTypes());
		model.addAttribute("jobs", jobService.getAllJobs());
		model.addAttribute("rarities", rarityService.getAllRarities());
		model.addAttribute("stats", statService.getAllStats());
		model.addAttribute("setBonuses", setBonusService.getAll());
		model.addAttribute("isAdd", true);

		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/equipment/equipment-form.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "equipment");

		return "layout/main";
	}

	@GetMapping("/edit")
	public String editForm(@RequestParam Long itemId, Model model) {

		model.addAttribute("item", equipmentService.getById(itemId));
		model.addAttribute("itemTypes", itemTypeService.getAllItemTypes());
		model.addAttribute("jobs", jobService.getAllJobs());
		model.addAttribute("rarities", rarityService.getAllRarities());
		model.addAttribute("stats", statService.getAllStats());
		model.addAttribute("setBonuses", setBonusService.getAll());
		model.addAttribute("isAdd", false);

		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/equipment/equipment-form.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "equipment");

		return "layout/main";
	}

	@GetMapping("/clone")
	public String cloneForm(@RequestParam Long itemId, Model model) {

		EquipmentItem original = equipmentService.getById(itemId);

		if (original == null) {
			return "redirect:/master/equipment";
		}

		original.setItemId(null);

		model.addAttribute("item", original);
		model.addAttribute("itemTypes", itemTypeService.getAllItemTypes());
		model.addAttribute("jobs", jobService.getAllJobs());
		model.addAttribute("rarities", rarityService.getAllRarities());
		model.addAttribute("stats", statService.getAllStats());
		model.addAttribute("setBonuses", setBonusService.getAll());
		model.addAttribute("isAdd", true);

		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/equipment/equipment-form.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "equipment");

		return "layout/main";
	}

	@PostMapping("/save")
	public String save(
			@ModelAttribute EquipmentItem item,
			@RequestParam boolean isAdd,
			@RequestParam(value = "iconFile", required = false) MultipartFile iconFile,
			@RequestParam(value = "keepOldIcon", required = false) String keepOldIcon) {

		try {
			prepareStats(item);

			boolean hasNewIcon = (iconFile != null && !iconFile.isEmpty());
			boolean keepOld = "true".equalsIgnoreCase(keepOldIcon) || "on".equalsIgnoreCase(keepOldIcon);

			if (hasNewIcon) {
				validateImage(iconFile);

				String ext = getFileExtension(iconFile.getOriginalFilename(), iconFile.getContentType());
				String safeName = buildSafeIconName(item.getName(), ext);

				item.setIconBlob(iconFile.getBytes());
				item.setIconMime(iconFile.getContentType());
				item.setIconName(safeName);

			} else if (!isAdd && keepOld) {
				EquipmentItem oldItem = equipmentService.getById(item.getItemId());
				if (oldItem != null) {
					item.setIconBlob(oldItem.getIconBlob());
					item.setIconMime(oldItem.getIconMime());
					item.setIconName(oldItem.getIconName());
				}
			} else if (!isAdd) {
				item.setIconBlob(null);
				item.setIconMime(null);
				item.setIconName(null);
			}

			equipmentService.save(item, isAdd);
			return "redirect:/master/equipment";

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage() == null ? "Save equipment failed" : e.getMessage(), e);
		}
	}

	@PostMapping("/delete")
	public String delete(@RequestParam Long itemId) {
		equipmentService.delete(itemId);
		return "redirect:/master/equipment";
	}

	@GetMapping("/icon")
	public ResponseEntity<byte[]> equipmentIcon(@RequestParam("itemId") Long itemId) {

		try {
			EquipmentItem item = equipmentService.getIconById(itemId);

			if (item == null || item.getIconBlob() == null || item.getIconBlob().length == 0) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}

			MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
			if (item.getIconMime() != null && !item.getIconMime().trim().isEmpty()) {
				mediaType = MediaType.parseMediaType(item.getIconMime());
			}

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(mediaType);
			headers.setCacheControl("no-store, no-cache, must-revalidate, max-age=0");

			return new ResponseEntity<byte[]>(item.getIconBlob(), headers, HttpStatus.OK);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	private void prepareStats(EquipmentItem item) {
		if (item != null && item.getStats() != null) {
			for (EquipmentItemStat s : item.getStats()) {
				if (item.getItemId() != null) {
					s.setItemId(item.getItemId());
				}
				if (s.getIsPercentage() == null) {
					s.setIsPercentage(0);
				}
			}
		}
	}

	private void validateImage(MultipartFile file) {
		String contentType = file.getContentType();

		if (contentType == null) {
			throw new RuntimeException("Icon content type is invalid");
		}

		if (!"image/png".equalsIgnoreCase(contentType)
				&& !"image/jpeg".equalsIgnoreCase(contentType)
				&& !"image/webp".equalsIgnoreCase(contentType)) {
			throw new RuntimeException("Icon must be PNG/JPG/WEBP");
		}

		long maxBytes = 2L * 1024L * 1024L;
		if (file.getSize() > maxBytes) {
			throw new RuntimeException("Icon file too large (max 2MB)");
		}
	}

	private String buildSafeIconName(String name, String ext) {
		String safe = name == null ? "equipment" : name.trim().toLowerCase();
		safe = safe.replaceAll("\\s+", "_");
		safe = safe.replaceAll("[^a-z0-9_]", "");
		safe = safe.replaceAll("_+", "_");
		safe = safe.replaceAll("^_+|_+$", "");

		if (safe.isEmpty()) {
			safe = "equipment";
		}

		return safe + ext;
	}

	private String getFileExtension(String originalFilename, String contentType) {
		if (originalFilename != null) {
			int lastDot = originalFilename.lastIndexOf('.');
			if (lastDot >= 0 && lastDot < originalFilename.length() - 1) {
				return originalFilename.substring(lastDot).toLowerCase();
			}
		}

		if ("image/png".equalsIgnoreCase(contentType)) {
			return ".png";
		}
		if ("image/jpeg".equalsIgnoreCase(contentType)) {
			return ".jpg";
		}
		if ("image/webp".equalsIgnoreCase(contentType)) {
			return ".webp";
		}

		return "";
	}
}