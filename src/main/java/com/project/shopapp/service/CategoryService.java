package com.project.shopapp.service;

import com.project.shopapp.dto.CategoryDTO;
import com.project.shopapp.entity.Category;
import com.project.shopapp.exception.AppException;
import com.project.shopapp.exception.ErrorCode;
import com.project.shopapp.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService{

    private final CategoryRepository categoryRepository;

    @Override
    public Category createCategory(CategoryDTO categoryDTO) {
        if(categoryRepository.existsByName(categoryDTO.getName())){
            throw new AppException(ErrorCode.CATEGORY_EXISTED);
        }

        Category category = Category.builder()
                .name(categoryDTO.getName())
                .build();
        return categoryRepository.save(category);
    }

    @Override
    public Category getCategoryById(long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(()->new AppException(ErrorCode.CATEGORY_NOT_EXISTED));
    }

    @Override
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    @Override
    public Category updateCategory(long categoryId, CategoryDTO categoryDTO) {
        Category existingCategory = getCategoryById(categoryId);
        existingCategory.setName(categoryDTO.getName());
        return categoryRepository.save(existingCategory);
    }

    @Override
    public void deleteCategory(long categoryId) {
        Category existingCategory = getCategoryById(categoryId);
        categoryRepository.deleteById(categoryId);
    }
}
