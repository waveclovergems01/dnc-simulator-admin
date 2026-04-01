package com.dnc.simulator.controller;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dnc.simulator.model.JsonExportConfig;
import com.dnc.simulator.model.export.ImageExportItem;
import com.dnc.simulator.service.JsonExportConfigService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

@Controller
public class JsonExportController {

	private final JsonExportConfigService jsonExportConfigService;

	public JsonExportController(JsonExportConfigService jsonExportConfigService) {
		this.jsonExportConfigService = jsonExportConfigService;
	}

	@GetMapping("/json/export")
	public String exportJson(Model model) {

		List<JsonExportConfig> configs = jsonExportConfigService.getAllConfigs();

		model.addAttribute("configs", configs);
		model.addAttribute("contentPage", "/WEB-INF/views/pages/json/export.jsp");
		model.addAttribute("activeMenuGroup", "json");
		model.addAttribute("activeMenu", "export");

		return "layout/main";
	}

	@PostMapping("/json/export/run")
	public void exportRun(
			@RequestParam("exportIds") List<Integer> exportIds,
			@RequestParam(value = "imageComponents", required = false) List<String> imageComponents,
			HttpServletResponse response) throws Exception {

		if (exportIds == null || exportIds.isEmpty()) {
			response.sendError(400, "No export selected");
			return;
		}

		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		response.setContentType("application/zip");
		response.setHeader("Content-Disposition", "attachment; filename=\"json_export.zip\"");

		ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream());

		try {
			Set<String> usedEntryNames = new HashSet<String>();

			/*
			 * ========================= JSON =========================
			 */
			for (Integer id : exportIds) {

				JsonExportConfig config = jsonExportConfigService.getConfigById(id);
				if (config == null) {
					continue;
				}

				String json = jsonExportConfigService.runExportSql(id);
				if (json == null || json.trim().isEmpty()) {
					continue;
				}

				JsonElement jsonElement = JsonParser.parseString(json);
				String prettyJson = gson.toJson(jsonElement);

				String entryName = safeZipEntryName(config.getExportFileName());
				entryName = ensureUniqueEntryName(entryName, usedEntryNames);

				zipOut.putNextEntry(new ZipEntry(entryName));
				zipOut.write(prettyJson.getBytes(StandardCharsets.UTF_8));
				zipOut.closeEntry();
			}

			/*
			 * ========================= IMAGES =========================
			 */
			if (imageComponents != null && !imageComponents.isEmpty()) {
				exportSelectedImageComponents(imageComponents, zipOut, usedEntryNames);
			}

			zipOut.finish();
		} finally {
			zipOut.close();
		}
	}

	private void exportSelectedImageComponents(List<String> imageComponents, ZipOutputStream zipOut,
			Set<String> usedEntryNames) throws Exception {

		for (String component : imageComponents) {
			if ("PLATE".equalsIgnoreCase(component)) {
				String basePath = jsonExportConfigService.getConfigValue("JSON", "EXPORT_PLATE");
				List<ImageExportItem> items = jsonExportConfigService.getPlateImages();
				writeImageEntries(basePath, items, zipOut, usedEntryNames);
			}

			if ("CARD".equalsIgnoreCase(component)) {
				String basePath = jsonExportConfigService.getConfigValue("JSON", "EXPORT_CARD");
				List<ImageExportItem> items = jsonExportConfigService.getCardImages();
				writeImageEntries(basePath, items, zipOut, usedEntryNames);
			}
		}
	}

	private void writeImageEntries(String basePath, List<ImageExportItem> items, ZipOutputStream zipOut,
			Set<String> usedEntryNames) throws Exception {

		String normalizedPath = normalizeFolderPath(basePath);

		if (items == null) {
			items = new ArrayList<>();
		}

		for (ImageExportItem item : items) {
			if (item == null || item.getFileBlob() == null || item.getFileBlob().length == 0) {
				continue;
			}

			String fileName = buildFileName(item);
			String entryName = safeZipEntryName(normalizedPath + fileName);
			entryName = ensureUniqueEntryName(entryName, usedEntryNames);

			zipOut.putNextEntry(new ZipEntry(entryName));
			zipOut.write(item.getFileBlob());
			zipOut.closeEntry();
		}
	}

	private String normalizeFolderPath(String path) {

		String result = path == null ? "" : path.trim();

		if (result.isEmpty()) {
			result = "src/assets/img/";
		}

		result = result.replace("\\", "/");

		while (result.startsWith("/")) {
			result = result.substring(1);
		}

		if (!result.endsWith("/")) {
			result = result + "/";
		}

		return result;
	}

	private String buildFileName(ImageExportItem item) {

		String fileName = item.getFileName() == null ? "" : item.getFileName().trim();

		if (!fileName.isEmpty()) {
			return sanitizeFileName(fileName);
		}

		return "image_" + item.getId() + guessExtension(item.getMimeType());
	}

	private String sanitizeFileName(String fileName) {

		String result = fileName == null ? "" : fileName.trim();

		result = result.replace("\\", "/");

		int slashIndex = result.lastIndexOf('/');
		if (slashIndex > -1) {
			result = result.substring(slashIndex + 1);
		}

		result = result.replaceAll("[\\\\/:*?\"<>|]", "_");

		if (result.isEmpty()) {
			result = "unnamed.png";
		}

		return result;
	}

	private String guessExtension(String mime) {

		if (mime == null) {
			return ".png";
		}

		String m = mime.trim().toLowerCase();

		if ("image/png".equals(m)) {
			return ".png";
		}
		if ("image/jpeg".equals(m) || "image/jpg".equals(m)) {
			return ".jpg";
		}
		if ("image/webp".equals(m)) {
			return ".webp";
		}
		if ("image/gif".equals(m)) {
			return ".gif";
		}

		return ".bin";
	}

	private String safeZipEntryName(String entryName) {

		String result = entryName == null ? "" : entryName.trim();

		result = result.replace("\\", "/");

		while (result.startsWith("/")) {
			result = result.substring(1);
		}

		return result;
	}

	private String ensureUniqueEntryName(String entryName, Set<String> usedEntryNames) {

		if (!usedEntryNames.contains(entryName)) {
			usedEntryNames.add(entryName);
			return entryName;
		}

		int dotIndex = entryName.lastIndexOf('.');
		String baseName = entryName;
		String extension = "";

		if (dotIndex > -1) {
			baseName = entryName.substring(0, dotIndex);
			extension = entryName.substring(dotIndex);
		}

		int index = 1;
		String candidate = baseName + "_" + index + extension;

		while (usedEntryNames.contains(candidate)) {
			index++;
			candidate = baseName + "_" + index + extension;
		}

		usedEntryNames.add(candidate);
		return candidate;
	}
}