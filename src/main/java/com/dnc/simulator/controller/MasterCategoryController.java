package com.dnc.simulator.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dnc.simulator.model.Category;
import com.dnc.simulator.service.CategoryService;

@Controller
public class MasterCategoryController {

	private final CategoryService categoryService;

	public MasterCategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	/*
	 * ========================= LIST =========================
	 */
	@GetMapping("/master/categories")
	public String categories(Model model) {

		List<Category> categories = categoryService.getAllCategories();

		model.addAttribute("categories", categories);
		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/categories.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "categories");

		return "layout/main";
	}

	/*
	 * ========================= ADD FORM =========================
	 */
	@GetMapping("/master/categories/add")
	public String addCategoryForm(Model model) {

		model.addAttribute("category", new Category());
		model.addAttribute("isAdd", true);

		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/category-form.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "categories");

		return "layout/main";
	}

	/*
	 * ========================= EDIT FORM =========================
	 */
	@GetMapping("/master/categories/edit")
	public String editCategoryForm(@RequestParam int id, Model model) {

		Category category = categoryService.getCategoryById(id);
		if (category == null) {
			return "redirect:/master/categories";
		}

		model.addAttribute("category", category);
		model.addAttribute("isAdd", false);

		model.addAttribute("contentPage", "/WEB-INF/views/pages/master/category-form.jsp");
		model.addAttribute("activeMenuGroup", "master");
		model.addAttribute("activeMenu", "categories");

		return "layout/main";
	}

	/*
	 * ========================= SAVE (INSERT / UPDATE) =========================
	 */
	@PostMapping("/master/categories/save")
	public String saveCategory(@RequestParam int categoryId, @RequestParam String categoryName,
			@RequestParam boolean isAdd) {

		Category category = new Category();
		category.setCategoryId(categoryId);
		category.setCategoryName(categoryName);

		if(isAdd) {
			categoryService.saveCategory(category);
		}else {
			categoryService.updateCategory(category);
		}
		

		return "redirect:/master/categories";
	}

	/*
	 * ========================= DELETE =========================
	 */
	@PostMapping("/master/categories/delete")
	public String deleteCategory(@RequestParam int id) {
		categoryService.deleteCategory(id);
		return "redirect:/master/categories";
	}
}
