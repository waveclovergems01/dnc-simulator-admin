package com.dnc.simulator.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dnc.simulator.model.Category;
import com.dnc.simulator.repository.CategoryRepository;
import com.dnc.simulator.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

	private final CategoryRepository categoryRepository;

	public CategoryServiceImpl(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	@Override
	public List<Category> getAllCategories() {
		return categoryRepository.findAll();
	}

	@Override
	public Category getCategoryById(int id) {
		return categoryRepository.findById(id);
	}

	@Override
	public void saveCategory(Category category) {
		categoryRepository.insert(category);
	}

	@Override
	public void updateCategory(Category category) {
		categoryRepository.update(category);
	}

	@Override
	public void deleteCategory(int id) {
		categoryRepository.delete(id);
	}
}
