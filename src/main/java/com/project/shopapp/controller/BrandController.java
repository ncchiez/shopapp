package com.project.shopapp.controller;

import com.project.shopapp.dto.BrandDTO;
import com.project.shopapp.dto.CategoryDTO;
import com.project.shopapp.entity.Brand;
import com.project.shopapp.entity.Category;
import com.project.shopapp.response.ApiResponse;
import com.project.shopapp.service.BrandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/brands")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    @PostMapping("")
    ResponseEntity<?> createBrand(@RequestBody @Valid BrandDTO brandDTO){
        Brand brand = brandService.createBrand(brandDTO);

        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .payload(brand)
                .build());
    }

    @GetMapping("")
    ResponseEntity<ApiResponse<?>> getAllBrand(){

        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .payload(brandService.getAllBrand())
                .build());
    }

    @GetMapping("/{id}")
    ResponseEntity<ApiResponse<?>> getBrandById (@PathVariable("id") long Id){

        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .payload(brandService.getBrandById(Id))
                .build());
    }

    @PutMapping("/{id}")
    ResponseEntity<?> updateBrand (@PathVariable long id, @RequestBody BrandDTO brandDTO){
        Brand brand = brandService.updateBrand(id,brandDTO);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .payload(brand)
                .build());
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteBrand (@PathVariable long id){
        brandService.deleteBrand(id);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Brand deleted")
                .build());
    }
}
