package com.project.shopapp.controller;

import com.project.shopapp.response.ApiResponse;
import com.project.shopapp.dto.CategoryDTO;
import com.project.shopapp.entity.Category;
import com.project.shopapp.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("")
    ResponseEntity<?> createCategory(@RequestBody @Valid CategoryDTO categoryDTO){
        Category category = categoryService.createCategory(categoryDTO);

        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .payload(category)
                .build());
    }

    @GetMapping("")
    ResponseEntity<ApiResponse<?>> getAllCategory(){

        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .payload(categoryService.getAllCategory())
                .build());
    }

    @GetMapping("/{id}")
    ResponseEntity<ApiResponse<?>> getCategoryById (@PathVariable("id") long Id){

        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .payload(categoryService.getCategoryById(Id))
                .build());
    }

    @PutMapping("/{id}")
    ResponseEntity<?> updateCategory (@PathVariable long id, @RequestBody CategoryDTO categoryDTO){
        Category category = categoryService.updateCategory(id,categoryDTO);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .payload(category)
                .build());
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteCategory (@PathVariable long id){
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Category deleted")
                .build());
    }
}
