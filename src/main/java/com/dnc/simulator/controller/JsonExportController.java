package com.dnc.simulator.controller;

import java.nio.charset.StandardCharsets;
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

		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		response.setContentType("application/zip");
		response.setHeader("Content-Disposition", "attachment; filename=\"json_export.zip\"");

		// ⭐ zip เขียนเข้า response ตรง ๆ
		ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream());

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

			zipOut.putNextEntry(new ZipEntry(config.getExportFileName()));
			zipOut.write(prettyJson.getBytes(StandardCharsets.UTF_8));
			zipOut.closeEntry();
		}

		zipOut.finish();
		zipOut.close();
	}

}
