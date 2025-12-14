package com.dnc.simulator.repository;

import java.util.List;
import com.dnc.simulator.model.Category;

public interface CategoryRepository {

	List<Category> findAll();

	Category findById(int id);

	void insert(Category category);

	void update(Category category);

	void delete(int id);
}
