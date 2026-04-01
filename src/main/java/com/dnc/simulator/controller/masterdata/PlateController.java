package com.dnc.simulator.controller.masterdata;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.dnc.simulator.model.plate.Plate;
import com.dnc.simulator.model.plate.PlateName;
import com.dnc.simulator.model.plate.PlateThirdStat;
import com.dnc.simulator.service.ItemTypeService;
import com.dnc.simulator.service.RarityService;
import com.dnc.simulator.service.StatService;
import com.dnc.simulator.service.patch.PatchLevelService;
import com.dnc.simulator.service.plate.PlateNameService;
import com.dnc.simulator.service.plate.PlateService;
import com.dnc.simulator.service.plate.PlateThirdStatService;

@Controller
@RequestMapping("/master/plate")
public class PlateController {

	@Autowired
	private PlateService plateService;

	@Autowired
	private ItemTypeService itemTypeService;

	@Autowired
	private PatchLevelService patchLevelService;

	@Autowired
	private RarityService rarityService;

	@Autowired
	private StatService statService;

	@Autowired
	private PlateThirdStatService plateThirdStatService;

	@Autowired
	private PlateNameService plateNameService;

	@GetMapping("/viewPlate")
	public String viewPlate(Model model) {
		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/plate/viewPlate.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "plate");
		model.addAttribute("plateList", plateService.findAll());
		return "layout/main";
	}

	@GetMapping("/addPlate")
	public String addPlate(Model model) {
		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/plate/addPlate.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "plate");

		model.addAttribute("mode", "ADD");
		model.addAttribute("itemTypeList", itemTypeService.getAllItemTypes());
		model.addAttribute("plateNameList", plateNameService.findAll());
		model.addAttribute("levelList", patchLevelService.findAll());
		model.addAttribute("rarityList", rarityService.getAllRarities());
		model.addAttribute("statList", statService.getAllStats());

		return "layout/main";
	}

	@GetMapping("/editPlate")
	public String editPlate(@RequestParam("id") Long id, Model model) {

		Plate plate = plateService.findById(id);
		if (plate == null) {
			return "redirect:/master/plate/viewPlate";
		}

		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/plate/addPlate.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "plate");

		model.addAttribute("mode", "EDIT");
		model.addAttribute("plate", plate);
		model.addAttribute("itemTypeList", itemTypeService.getAllItemTypes());
		model.addAttribute("plateNameList", plateNameService.findAll());
		model.addAttribute("levelList", patchLevelService.findAll());
		model.addAttribute("rarityList", rarityService.getAllRarities());
		model.addAttribute("statList", statService.getAllStats());

		return "layout/main";
	}

	@PostMapping("/savePlate")
	@ResponseBody
	public ResponseEntity<?> savePlate(@RequestParam(value = "id", required = false) Long id,
			@RequestParam("typeId") String typeIdStr,
			@RequestParam("plateNameId") String plateNameIdStr,
			@RequestParam("patchLevelId") String patchLevelIdStr,
			@RequestParam("rarityId") String rarityIdStr,
			@RequestParam(value = "statId", required = false) String statIdStr,
			@RequestParam(value = "statValue", required = false) String statValueStr,
			@RequestParam(value = "statPercent", required = false) String statPercentStr) {

		try {
			typeIdStr = safeTrim(typeIdStr);
			plateNameIdStr = safeTrim(plateNameIdStr);
			patchLevelIdStr = safeTrim(patchLevelIdStr);
			rarityIdStr = safeTrim(rarityIdStr);
			statIdStr = safeTrim(statIdStr);
			statValueStr = safeTrim(statValueStr);
			statPercentStr = safeTrim(statPercentStr);

			if (!isNumber(typeIdStr)) {
				return ResponseEntity.badRequest().body("Item Type is required");
			}
			if (!isNumber(plateNameIdStr)) {
				return ResponseEntity.badRequest().body("Plate Name is required");
			}
			if (!isNumber(patchLevelIdStr)) {
				return ResponseEntity.badRequest().body("Patch Level is required");
			}
			if (!isNumber(rarityIdStr)) {
				return ResponseEntity.badRequest().body("Rarity is required");
			}

			if ("1".equals(typeIdStr) && !isNumber(statIdStr)) {
				return ResponseEntity.badRequest().body("Stat is required when Item Type is 1");
			}

			if (!statValueStr.isEmpty() && !statValueStr.matches("^\\d+$")) {
				return ResponseEntity.badRequest().body("Value (Unit) must be numeric only");
			}

			if (!statPercentStr.isEmpty() && !statPercentStr.matches("^\\d+(\\.\\d+)?$")) {
				return ResponseEntity.badRequest().body("Value (%) must be numeric only");
			}

			Integer typeId = Integer.valueOf(typeIdStr);
			Long plateNameId = Long.valueOf(plateNameIdStr);
			Long patchLevelId = Long.valueOf(patchLevelIdStr);
			Integer rarityId = Integer.valueOf(rarityIdStr);

			Integer statId = statIdStr.isEmpty() ? null : Integer.valueOf(statIdStr);
			Integer statValue = statValueStr.isEmpty() ? null : Integer.valueOf(statValueStr);
			Double statPercent = statPercentStr.isEmpty() ? null : Double.valueOf(statPercentStr);

			if (plateService.existsDuplicate(typeId, patchLevelId, plateNameId, rarityId, id)) {
				return ResponseEntity.badRequest()
						.body("Duplicate plate: same Item Type + Patch Level + Plate Name + Rarity already exists");
			}

			if (id == null) {
				plateService.create(typeId, patchLevelId, plateNameId, rarityId, statId, statValue, statPercent);
			} else {
				plateService.update(id, typeId, patchLevelId, plateNameId, rarityId, statId, statValue, statPercent);
			}

			return ResponseEntity.ok("SUCCESS");

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(e.getMessage() == null ? "ERROR" : e.getMessage());
		}
	}

	@PostMapping("/deletePlate")
	@ResponseBody
	public ResponseEntity<?> deletePlate(@RequestParam("id") Long id) {
		try {
			plateService.delete(id);
			return ResponseEntity.ok("SUCCESS");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(e.getMessage() == null ? "ERROR" : e.getMessage());
		}
	}

	@GetMapping("/plateIcon")
	public ResponseEntity<byte[]> plateIcon(@RequestParam("id") Long id) {
		try {
			Plate plate = plateService.findIconById(id);

			if (plate == null || plate.getIconBlob() == null || plate.getIconBlob().length == 0) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}

			MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
			if (plate.getIconMime() != null && !plate.getIconMime().trim().isEmpty()) {
				mediaType = MediaType.parseMediaType(plate.getIconMime());
			}

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(mediaType);
			headers.setCacheControl("no-store, no-cache, must-revalidate, max-age=0");

			return new ResponseEntity<>(plate.getIconBlob(), headers, HttpStatus.OK);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@GetMapping("/view3rdStat")
	public String view3rdStat(Model model) {

		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/plate/view3rdStat.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "plate");

		model.addAttribute("thirdStatList", plateThirdStatService.findAll());

		return "layout/main";
	}

	@GetMapping("/add3rdStat")
	public String add3rdStat(Model model) {

		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/plate/add3rdStat.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "plate");

		model.addAttribute("mode", "ADD");
		model.addAttribute("statList", statService.getAllStats());
		model.addAttribute("rarityList", rarityService.getAllRarities());
		model.addAttribute("levelList", patchLevelService.findAll());

		return "layout/main";
	}

	@GetMapping("/edit3rdStat")
	public String edit3rdStat(@RequestParam("id") Long id, Model model) {

		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/plate/add3rdStat.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "plate");

		PlateThirdStat thirdStat = plateThirdStatService.findById(id);
		if (thirdStat == null) {
			return "redirect:/master/plate/view3rdStat";
		}

		model.addAttribute("mode", "EDIT");
		model.addAttribute("thirdStat", thirdStat);
		model.addAttribute("statList", statService.getAllStats());
		model.addAttribute("rarityList", rarityService.getAllRarities());
		model.addAttribute("levelList", patchLevelService.findAll());

		return "layout/main";
	}

	@PostMapping("/save3rdStat")
	@ResponseBody
	public ResponseEntity<?> save3rdStat(@RequestParam(value = "id", required = false) Long id,
			@RequestParam("statId") String statIdStr, @RequestParam("rarityId") String rarityIdStr,
			@RequestParam("patchLevelId") String patchLevelIdStr, @RequestParam("value") String valueStr) {

		try {
			statIdStr = statIdStr == null ? "" : statIdStr.trim();
			rarityIdStr = rarityIdStr == null ? "" : rarityIdStr.trim();
			patchLevelIdStr = patchLevelIdStr == null ? "" : patchLevelIdStr.trim();
			valueStr = valueStr == null ? "" : valueStr.trim();

			if (statIdStr.isEmpty() || !statIdStr.matches("^\\d+$")) {
				return ResponseEntity.badRequest().body("Stat is required");
			}

			if (rarityIdStr.isEmpty() || !rarityIdStr.matches("^\\d+$")) {
				return ResponseEntity.badRequest().body("Rarity is required");
			}

			if (patchLevelIdStr.isEmpty() || !patchLevelIdStr.matches("^\\d+$")) {
				return ResponseEntity.badRequest().body("Patch Level is required");
			}

			if (valueStr.isEmpty() || !valueStr.matches("^\\d+$")) {
				return ResponseEntity.badRequest().body("Value must be numeric only");
			}

			Integer statId = Integer.valueOf(statIdStr);
			Integer rarityId = Integer.valueOf(rarityIdStr);
			Long patchLevelId = Long.valueOf(patchLevelIdStr);
			Integer value = Integer.valueOf(valueStr);

			if (id == null) {
				plateThirdStatService.create(statId, rarityId, patchLevelId, value);
			} else {
				plateThirdStatService.update(id, statId, rarityId, patchLevelId, value);
			}

			return ResponseEntity.ok("SUCCESS");

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(e.getMessage() == null ? "ERROR" : e.getMessage());
		}
	}

	@PostMapping("/delete3rdStat")
	public String delete3rdStat(@RequestParam("id") Long id) {
		plateThirdStatService.delete(id);
		return "redirect:/master/plate/view3rdStat";
	}

	@GetMapping("/viewPlateName")
	public String viewPlateName(Model model) {

		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/plate/viewPlateName.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "plate");

		model.addAttribute("plateNameList", plateNameService.findAll());

		return "layout/main";
	}

	@GetMapping("/addPlateName")
	public String addPlateName(Model model) {

		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/plate/addPlateName.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "plate");

		model.addAttribute("mode", "ADD");

		return "layout/main";
	}

	@GetMapping("/editPlateName")
	public String editPlateName(@RequestParam("id") Long id, Model model) {

		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/plate/addPlateName.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "plate");

		model.addAttribute("mode", "EDIT");

		PlateName plateName = plateNameService.findById(id);
		if (plateName == null) {
			return "redirect:/master/plate/viewPlateName";
		}

		model.addAttribute("plateName", plateName);

		return "layout/main";
	}

	@PostMapping("/savePlateName")
	@ResponseBody
	public ResponseEntity<?> savePlateName(@RequestParam(value = "id", required = false) Long id,
			@RequestParam("name") String name,
			@RequestParam(value = "iconFile", required = false) MultipartFile iconFile) {

		try {
			name = name == null ? "" : name.trim();

			if (name.isEmpty()) {
				return ResponseEntity.badRequest().body("Plate Name is required");
			}

			if (!name.matches("^[A-Za-z]+( [A-Za-z]+)*$")) {
				return ResponseEntity.badRequest()
						.body("Plate Name must contain only English words separated by spaces");
			}

			byte[] iconBlob = null;
			String iconMime = null;
			String iconName = null;

			String safePlateName = name.trim().toLowerCase();
			safePlateName = safePlateName.replaceAll("\\s+", "_");
			safePlateName = safePlateName.replaceAll("[^a-z0-9_]", "");
			safePlateName = safePlateName.replaceAll("_+", "_");
			safePlateName = safePlateName.replaceAll("^_+|_+$", "");

			boolean hasNewIcon = (iconFile != null && !iconFile.isEmpty());

			if (hasNewIcon) {
				String ct = iconFile.getContentType();
				if (ct == null || !(ct.equals("image/png") || ct.equals("image/jpeg") || ct.equals("image/webp"))) {
					return ResponseEntity.badRequest().body("Icon must be PNG/JPG/WEBP");
				}

				long maxBytes = 2L * 1024L * 1024L;
				if (iconFile.getSize() > maxBytes) {
					return ResponseEntity.badRequest().body("Icon file too large (max 2MB)");
				}

				String ext = getFileExtension(iconFile.getOriginalFilename(), ct);

				iconBlob = iconFile.getBytes();
				iconMime = ct;
				iconName = safePlateName + ext;
			}

			if (id == null) {
				plateNameService.create(name, iconBlob, iconMime, iconName);
			} else {
				if (hasNewIcon) {
					plateNameService.update(id, name, iconBlob, iconMime, iconName);
				} else {
					plateNameService.updateNoIcon(id, name);
				}
			}

			return ResponseEntity.ok("SUCCESS");

		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Icon read error");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(e.getMessage() == null ? "ERROR" : e.getMessage());
		}
	}

	@GetMapping("/plateNameIcon")
	public ResponseEntity<byte[]> plateNameIcon(@RequestParam("id") Long id) {

		try {
			PlateName x = plateNameService.findIconById(id);

			if (x == null || x.getIconBlob() == null || x.getIconBlob().length == 0) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}

			MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
			if (x.getIconMime() != null && !x.getIconMime().trim().isEmpty()) {
				mediaType = MediaType.parseMediaType(x.getIconMime());
			}

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(mediaType);
			headers.setCacheControl("no-store, no-cache, must-revalidate, max-age=0");

			return new ResponseEntity<>(x.getIconBlob(), headers, HttpStatus.OK);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@PostMapping("/deletePlateName")
	public String deletePlateName(@RequestParam("id") Long id) {

		plateNameService.delete(id);

		return "redirect:/master/plate/viewPlateName";
	}

	// =========================
	// UTIL
	// =========================
	private String safeTrim(String s) {
		return s == null ? "" : s.trim();
	}

	private boolean isNumber(String s) {
		return s != null && s.matches("^\\d+$");
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