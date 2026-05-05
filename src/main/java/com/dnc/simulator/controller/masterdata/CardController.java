package com.dnc.simulator.controller.masterdata;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

import com.dnc.simulator.model.ItemType;
import com.dnc.simulator.model.card.Card;
import com.dnc.simulator.model.card.CardName;
import com.dnc.simulator.model.card.CardStat;
import com.dnc.simulator.service.ItemTypeService;
import com.dnc.simulator.service.RarityService;
import com.dnc.simulator.service.StatService;
import com.dnc.simulator.service.card.CardNameService;
import com.dnc.simulator.service.card.CardService;
import com.dnc.simulator.service.patch.PatchLevelService;

@Controller
@RequestMapping("/master/card")
public class CardController {

	@Autowired
	private CardService cardService;

	@Autowired
	private CardNameService cardNameService;

	@Autowired
	private ItemTypeService itemTypeService;

	@Autowired
	private PatchLevelService patchLevelService;

	@Autowired
	private RarityService rarityService;

	@Autowired
	private StatService statService;

	@GetMapping("/viewCard")
	public String viewCard(Model model) {
		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/card/viewCard.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "card");
		model.addAttribute("cardList", cardService.findAll());
		return "layout/main";
	}

	@GetMapping("/addCard")
	public String addCard(Model model) {
		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/card/addCard.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "card");

		List<ItemType> cardItemTypeList = itemTypeService.getAllItemTypes().stream()
				.filter(item -> item.getCategoryId() == 60000).collect(Collectors.toList());

		model.addAttribute("mode", "ADD");
		model.addAttribute("itemTypeList", cardItemTypeList);
		model.addAttribute("cardNameList", cardNameService.findAll());
		model.addAttribute("levelList", patchLevelService.findAll());
		model.addAttribute("rarityList", rarityService.getAllRarities());
		model.addAttribute("statList", statService.getAllStats());

		return "layout/main";
	}

	@GetMapping("/editCard")
	public String editCard(@RequestParam("id") Long id, Model model) {

		Card card = cardService.findById(id);
		if (card == null) {
			return "redirect:/master/card/viewCard";
		}

		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/card/addCard.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "card");

		List<ItemType> cardItemTypeList = itemTypeService.getAllItemTypes().stream()
				.filter(item -> item.getCategoryId() == 40000).collect(Collectors.toList());

		model.addAttribute("mode", "EDIT");
		model.addAttribute("card", card);
		model.addAttribute("itemTypeList", cardItemTypeList);
		model.addAttribute("cardNameList", cardNameService.findAll());
		model.addAttribute("levelList", patchLevelService.findAll());
		model.addAttribute("rarityList", rarityService.getAllRarities());
		model.addAttribute("statList", statService.getAllStats());

		return "layout/main";
	}

	@PostMapping("/saveCard")
	@ResponseBody
	public ResponseEntity<?> saveCard(@RequestParam(value = "id", required = false) Long id,
			@RequestParam("typeId") String typeIdStr, @RequestParam("cardNameId") String cardNameIdStr,
			@RequestParam("patchLevelId") String patchLevelIdStr, @RequestParam("rarityId") String rarityIdStr,
			@RequestParam("slotNumber") String slotNumberStr,
			@RequestParam(value = "statId", required = false) List<String> statIdStrList,
			@RequestParam(value = "valueMin", required = false) List<String> valueMinStrList,
			@RequestParam(value = "valueMax", required = false) List<String> valueMaxStrList,
			@RequestParam(value = "isPercentage", required = false) List<String> isPercentageStrList) {

		try {
			typeIdStr = safeTrim(typeIdStr);
			cardNameIdStr = safeTrim(cardNameIdStr);
			patchLevelIdStr = safeTrim(patchLevelIdStr);
			rarityIdStr = safeTrim(rarityIdStr);
			slotNumberStr = safeTrim(slotNumberStr);

			if (!isNumber(typeIdStr)) {
				return ResponseEntity.badRequest().body("Item Type is required");
			}
			if (!isNumber(cardNameIdStr)) {
				return ResponseEntity.badRequest().body("Card Name is required");
			}
			if (!isNumber(patchLevelIdStr)) {
				return ResponseEntity.badRequest().body("Level is required");
			}
			if (!isNumber(rarityIdStr)) {
				return ResponseEntity.badRequest().body("Rarity is required");
			}
			if (!isNumber(slotNumberStr)) {
				return ResponseEntity.badRequest().body("Slot Number is required");
			}

			Integer slotNumber = Integer.valueOf(slotNumberStr);
			if (slotNumber <= 0) {
				return ResponseEntity.badRequest().body("Slot Number must be greater than 0");
			}

			List<CardStat> statList = buildStatList(statIdStrList, valueMinStrList, valueMaxStrList,
					isPercentageStrList);

			if (statList.isEmpty()) {
				return ResponseEntity.badRequest().body("At least one stat is required");
			}

			Integer typeId = Integer.valueOf(typeIdStr);
			Long cardNameId = Long.valueOf(cardNameIdStr);
			Long patchLevelId = Long.valueOf(patchLevelIdStr);
			Integer rarityId = Integer.valueOf(rarityIdStr);

			if (cardService.existsDuplicate(typeId, patchLevelId, cardNameId, rarityId, id)) {
				return ResponseEntity.badRequest()
						.body("Duplicate card: same Item Type + Level + Card Name + Rarity already exists");
			}

			if (id == null) {
				cardService.create(typeId, patchLevelId, cardNameId, rarityId, slotNumber, statList);
			} else {
				cardService.update(id, typeId, patchLevelId, cardNameId, rarityId, slotNumber, statList);
			}

			return ResponseEntity.ok("SUCCESS");

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(e.getMessage() == null ? "ERROR" : e.getMessage());
		}
	}

	@PostMapping("/deleteCard")
	@ResponseBody
	public ResponseEntity<?> deleteCard(@RequestParam("id") Long id) {
		try {
			cardService.delete(id);
			return ResponseEntity.ok("SUCCESS");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(e.getMessage() == null ? "ERROR" : e.getMessage());
		}
	}

	@GetMapping("/cardIcon")
	public ResponseEntity<byte[]> cardIcon(@RequestParam("id") Long id) {
		try {
			Card card = cardService.findIconById(id);

			if (card == null || card.getIconBlob() == null || card.getIconBlob().length == 0) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}

			MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
			if (card.getIconMime() != null && !card.getIconMime().trim().isEmpty()) {
				mediaType = MediaType.parseMediaType(card.getIconMime());
			}

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(mediaType);
			headers.setCacheControl("no-store, no-cache, must-revalidate, max-age=0");

			return new ResponseEntity<byte[]>(card.getIconBlob(), headers, HttpStatus.OK);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@GetMapping("/viewCardName")
	public String viewCardName(Model model) {
		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/card/viewCardName.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "card");
		model.addAttribute("cardNameList", cardNameService.findAll());
		return "layout/main";
	}

	@GetMapping("/addCardName")
	public String addCardName(Model model) {
		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/card/addCardName.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "card");
		model.addAttribute("mode", "ADD");
		return "layout/main";
	}

	@GetMapping("/editCardName")
	public String editCardName(@RequestParam("id") Long id, Model model) {
		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/card/addCardName.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "card");
		model.addAttribute("mode", "EDIT");

		CardName cardName = cardNameService.findById(id);
		if (cardName == null) {
			return "redirect:/master/card/viewCardName";
		}

		model.addAttribute("cardName", cardName);
		return "layout/main";
	}

	@PostMapping("/saveCardName")
	@ResponseBody
	public ResponseEntity<?> saveCardName(@RequestParam(value = "id", required = false) Long id,
			@RequestParam("name") String name,
			@RequestParam(value = "iconFile", required = false) MultipartFile iconFile) {

		try {
			name = safeTrim(name);

			if (name.isEmpty()) {
				return ResponseEntity.badRequest().body("Card Name is required");
			}

			if (!name.matches("^[A-Za-z0-9'\\-]+( [A-Za-z0-9'\\-]+)*$")) {
				return ResponseEntity.badRequest()
						.body("Card Name must contain only English letters, numbers, spaces, hyphen, and apostrophe");
			}

			byte[] iconBlob = null;
			String iconMime = null;
			String iconName = null;

			String safeCardName = name.trim().toLowerCase();
			safeCardName = safeCardName.replaceAll("\\s+", "_");
			safeCardName = safeCardName.replaceAll("[^a-z0-9_]", "");
			safeCardName = safeCardName.replaceAll("_+", "_");
			safeCardName = safeCardName.replaceAll("^_+|_+$", "");

			boolean hasNewIcon = iconFile != null && !iconFile.isEmpty();

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
				iconName = safeCardName + getFileExtension(iconFile.getOriginalFilename(), ct);
			}

			if (id == null) {
				cardNameService.create(name, iconBlob, iconMime, iconName);
			} else {
				if (hasNewIcon) {
					cardNameService.update(id, name, iconBlob, iconMime, iconName);
				} else {
					cardNameService.updateNoIcon(id, name);
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

	@GetMapping("/cardNameIcon")
	public ResponseEntity<byte[]> cardNameIcon(@RequestParam("id") Long id) {
		try {
			CardName x = cardNameService.findIconById(id);

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

			return new ResponseEntity<byte[]>(x.getIconBlob(), headers, HttpStatus.OK);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@PostMapping("/deleteCardName")
	public String deleteCardName(@RequestParam("id") Long id) {
		cardNameService.delete(id);
		return "redirect:/master/card/viewCardName";
	}

	private List<CardStat> buildStatList(List<String> statIdStrList, List<String> valueMinStrList,
			List<String> valueMaxStrList, List<String> isPercentageStrList) {

		List<CardStat> result = new ArrayList<CardStat>();

		if (statIdStrList == null || valueMinStrList == null || valueMaxStrList == null) {
			return result;
		}

		int size = Math.min(statIdStrList.size(), Math.min(valueMinStrList.size(), valueMaxStrList.size()));

		for (int i = 0; i < size; i++) {
			String statIdStr = safeTrim(statIdStrList.get(i));
			String valueMinStr = safeTrim(valueMinStrList.get(i));
			String valueMaxStr = safeTrim(valueMaxStrList.get(i));

			String isPercentageStr = "0";
			if (isPercentageStrList != null && i < isPercentageStrList.size()) {
				isPercentageStr = safeTrim(isPercentageStrList.get(i));
			}

			if (statIdStr.isEmpty() && valueMinStr.isEmpty() && valueMaxStr.isEmpty()) {
				continue;
			}

			if (!isNumber(statIdStr)) {
				throw new RuntimeException("Stat is required");
			}

			if (!valueMinStr.matches("^\\d+(\\.\\d+)?$")) {
				throw new RuntimeException("Min value must be numeric only");
			}

			if (!valueMaxStr.matches("^\\d+(\\.\\d+)?$")) {
				throw new RuntimeException("Max value must be numeric only");
			}

			if (!"0".equals(isPercentageStr) && !"1".equals(isPercentageStr)) {
				throw new RuntimeException("isPercentage must be 0 or 1");
			}

			Double valueMin = Double.valueOf(valueMinStr);
			Double valueMax = Double.valueOf(valueMaxStr);

			if (valueMin.doubleValue() > valueMax.doubleValue()) {
				throw new RuntimeException("Min value must be less than or equal Max value");
			}

			CardStat stat = new CardStat();
			stat.setStatId(Integer.valueOf(statIdStr));
			stat.setValueMin(valueMin);
			stat.setValueMax(valueMax);
			stat.setIsPercentage(Integer.valueOf(isPercentageStr));

			result.add(stat);
		}

		return result;
	}

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