package com.dnc.simulator.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.dnc.simulator.model.Category;
import com.dnc.simulator.repository.CategoryRepository;

@Repository
public class CategoryRepositoryImpl implements CategoryRepository {

	private final JdbcTemplate jdbcTemplate;

	public CategoryRepositoryImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private static final RowMapper<Category> ROW_MAPPER = new RowMapper<Category>() {
		@Override
		public Category mapRow(ResultSet rs, int rowNum) throws SQLException {
			Category c = new Category();
			c.setCategoryId(rs.getInt("category_id"));
			c.setCategoryName(rs.getString("category_name"));
			return c;
		}
	};

	@Override
	public List<Category> findAll() {
		String sql = "SELECT * FROM m_categories ORDER BY category_id";
		return jdbcTemplate.query(sql, ROW_MAPPER);
	}

	@Override
	public Category findById(int id) {
		String sql = "SELECT * FROM m_categories WHERE category_id = ?";
		List<Category> list = jdbcTemplate.query(sql, ROW_MAPPER, id);
		return list.isEmpty() ? null : list.get(0);
	}

	@Override
	public void insert(Category category) {
		String sql = "INSERT INTO m_categories (category_id, category_name) VALUES (?, ?)";
		jdbcTemplate.update(sql, category.getCategoryId(), category.getCategoryName());
	}

	@Override
	public void update(Category category) {
		String sql = "UPDATE m_categories SET category_name = ? WHERE category_id = ?";
		jdbcTemplate.update(sql, category.getCategoryName(), category.getCategoryId());
	}

	@Override
	public void delete(int id) {
		String sql = "DELETE FROM m_categories WHERE category_id = ?";
		jdbcTemplate.update(sql, id);
	}
}
