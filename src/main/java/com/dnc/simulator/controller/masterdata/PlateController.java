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
import com.dnc.simulator.service.RarityService;
import com.dnc.simulator.service.patch.PatchLevelService;
import com.dnc.simulator.service.plate.PlateService;
import com.dnc.simulator.service.plate.PlateTypeService;

@Controller
@RequestMapping("/master/plate")
public class PlateController {

	@Autowired
	private PlateService plateService;

	@Autowired
	private PlateTypeService plateTypeService;

	@Autowired
	private PatchLevelService patchLevelService;

	@Autowired
	private RarityService rarityService;

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

		model.addAttribute("typeList", plateTypeService.findAll());
		model.addAttribute("levelList", patchLevelService.findAll());
		model.addAttribute("rarityList", rarityService.getAllRarities());

		return "layout/main";
	}

	@GetMapping("/editPlate")
	public String editPlate(@RequestParam("id") Long id, Model model) {

		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/plate/addPlate.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "plate");

		model.addAttribute("mode", "EDIT");

		Plate plate = plateService.findById(id);
		if (plate == null) {
			return "redirect:/master/plate/viewPlate";
		}
		model.addAttribute("plate", plate);

		model.addAttribute("typeList", plateTypeService.findAll());
		model.addAttribute("levelList", patchLevelService.findAll());
		model.addAttribute("rarityList", rarityService.getAllRarities());

		return "layout/main";
	}

	@PostMapping("/savePlate")
	@ResponseBody
	public ResponseEntity<?> savePlate(@RequestParam(value = "id", required = false) Long id,
			@RequestParam("plateTypeId") String plateTypeIdStr,
			@RequestParam("patchLevelId") String patchLevelIdStr, @RequestParam("rarityId") String rarityIdStr,
			@RequestParam("plateName") String plateName,
			@RequestParam(value = "iconFile", required = false) MultipartFile iconFile) {

		try {
			plateTypeIdStr = plateTypeIdStr == null ? "" : plateTypeIdStr.trim();
			patchLevelIdStr = patchLevelIdStr == null ? "" : patchLevelIdStr.trim();
			rarityIdStr = rarityIdStr == null ? "" : rarityIdStr.trim();
			plateName = plateName == null ? "" : plateName.trim();

			if (plateTypeIdStr.isEmpty() || !plateTypeIdStr.matches("^\\d+$")) {
				return ResponseEntity.badRequest().body("Plate Type is required");
			}
			if (patchLevelIdStr.isEmpty() || !patchLevelIdStr.matches("^\\d+$")) {
				return ResponseEntity.badRequest().body("Patch Level is required");
			}
			if (rarityIdStr.isEmpty() || !rarityIdStr.matches("^\\d+$")) {
				return ResponseEntity.badRequest().body("Rarity is required");
			}
			if (plateName.isEmpty()) {
				return ResponseEntity.badRequest().body("Plate Name is required");
			}

			Long plateTypeId = Long.valueOf(plateTypeIdStr);
			Long patchLevelId = Long.valueOf(patchLevelIdStr);
			Integer rarityId = Integer.valueOf(rarityIdStr);

			byte[] iconBlob = null;
			String iconMime = null;
			String iconName = null;

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

				iconBlob = iconFile.getBytes();
				iconMime = ct;
				iconName = iconFile.getOriginalFilename();
			}

			// ===== save =====
			if (id == null) {
				plateService.create(plateTypeId, patchLevelId, rarityId, plateName, iconBlob, iconMime, iconName);
			} else {
				if (hasNewIcon) {
					plateService.update(id, plateTypeId, patchLevelId, rarityId, plateName, iconBlob, iconMime,
							iconName);
				} else {
					plateService.updateNoIcon(id, plateTypeId, patchLevelId, rarityId, plateName);
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

	@GetMapping("/plateIcon")
	public ResponseEntity<byte[]> plateIcon(@RequestParam("id") Long id) {

		try {
			Plate p = plateService.findIconById(id);

			if (p == null || p.getIconBlob() == null || p.getIconBlob().length == 0) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}

			MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
			if (p.getIconMime() != null && !p.getIconMime().trim().isEmpty()) {
				mediaType = MediaType.parseMediaType(p.getIconMime());
			}

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(mediaType);
			headers.setCacheControl("no-store, no-cache, must-revalidate, max-age=0");

			return new ResponseEntity<>(p.getIconBlob(), headers, HttpStatus.OK);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@PostMapping("/deletePlate")
	public String deletePlate(@RequestParam("id") Long id) {
		plateService.delete(id);
		return "redirect:/master/plate/viewPlate";
	}

	@GetMapping("/viewType")
	public String viewType(Model model) {
		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/plate/viewType.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "plate");

		model.addAttribute("typeList", plateTypeService.findAll());

		return "layout/main";
	}

	@GetMapping("/addType")
	public String addType(Model model) {
		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/plate/addType.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "plate");

		model.addAttribute("mode", "ADD");

		return "layout/main";
	}

	@GetMapping("/editType")
	public String editType(@RequestParam("id") Long id, Model model) {

		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/plate/addType.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "plate");

		model.addAttribute("mode", "EDIT");
		model.addAttribute("type", plateTypeService.findById(id));

		return "layout/main";
	}

	@PostMapping("/saveType")
	@ResponseBody
	public ResponseEntity<?> saveType(@RequestParam(value = "id", required = false) Long id,
			@RequestParam("typeName") String typeName) {
		try {

			typeName = typeName == null ? "" : typeName.trim();

			if (typeName.isEmpty()) {
				return ResponseEntity.badRequest().body("Type name is required");
			}

			if (!typeName.matches("^[A-Za-z ]+$")) {
				return ResponseEntity.badRequest().body("Type name must contain English letters only");
			}

			if (id == null) {
				plateTypeService.create(typeName);
			} else {
				plateTypeService.update(id, typeName);
			}

			return ResponseEntity.ok("SUCCESS");

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ERROR");
		}
	}

	@PostMapping("/deleteType")
	public String deleteType(@RequestParam("id") Long id) {

		plateTypeService.delete(id);

		return "redirect:/master/plate/viewType";
	}
}