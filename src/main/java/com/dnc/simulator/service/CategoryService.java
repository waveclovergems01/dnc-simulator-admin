package com.dnc.simulator.service;

import java.util.List;
import com.dnc.simulator.model.Category;

public interface CategoryService {

	List<Category> getAllCategories();

	Category getCategoryById(int id);

	void saveCategory(Category category);

	void updateCategory(Category category);

	void deleteCategory(int id);
}
