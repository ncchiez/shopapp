package com.project.shopapp.service;

import com.project.shopapp.dto.CategoryDTO;
import com.project.shopapp.entity.Category;

import java.util.List;

public interface ICategoryService {
    Category createCategory(CategoryDTO categoryDTO);
    Category getCategoryById(long categoryId);
    List<Category> getAllCategory();
    Category updateCategory(long categoryId, CategoryDTO categoryDTO);
    void deleteCategory(long categoryId);
}
