package com.dnc.simulator.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dnc.simulator.model.JsonExportConfig;
import com.dnc.simulator.service.JsonExportConfigService;

@Controller
public class JsonExportController {

	private final JsonExportConfigService jsonExportConfigService;

	public JsonExportController(JsonExportConfigService jsonExportConfigService) {
		this.jsonExportConfigService = jsonExportConfigService;
	}

	/*
	 * ========================= EXPORT PAGE =========================
	 */
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
	public void exportRun(@RequestParam("exportIds") List<Integer> exportIds, HttpServletResponse response)
			throws Exception {

		if (exportIds == null || exportIds.isEmpty()) {
			response.sendError(400, "No export selected");
			return;
		}

		File downloadDir = new File(System.getProperty("user.home"), "Downloads");
		if (!downloadDir.exists()) {
			downloadDir.mkdirs();
		}

		File zipFile = new File(downloadDir, "json_export.zip");
		ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));

		for (Integer id : exportIds) {

			JsonExportConfig config = jsonExportConfigService.getConfigById(id);

			if (config == null) {
				continue;
			}

			// 🔑 execute SQL → JSON string (1 row / 1 column)
			String json = jsonExportConfigService.runExportSql(id);

			if (json == null) {
				continue;
			}

			File jsonFile = new File(downloadDir, config.getExportFileName());

			Files.write(jsonFile.toPath(), json.getBytes(StandardCharsets.UTF_8));

			zipOut.putNextEntry(new ZipEntry(jsonFile.getName()));
			zipOut.write(Files.readAllBytes(jsonFile.toPath()));
			zipOut.closeEntry();
		}

		zipOut.close();

		// ⬇️ download zip (Java 8 compatible)
		response.setContentType("application/zip");
		response.setHeader("Content-Disposition", "attachment; filename=\"json_export.zip\"");

		FileInputStream fis = new FileInputStream(zipFile);
		byte[] buffer = new byte[4096];
		int len;

		while ((len = fis.read(buffer)) != -1) {
			response.getOutputStream().write(buffer, 0, len);
		}

		response.getOutputStream().flush();
		fis.close();
	}

}
