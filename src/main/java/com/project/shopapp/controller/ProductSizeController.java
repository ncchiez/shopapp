package com.project.shopapp.controller;

import com.project.shopapp.dto.CategoryDTO;
import com.project.shopapp.dto.ListProductSizeDTO;
import com.project.shopapp.dto.ProductDTO;
import com.project.shopapp.dto.ProductSizeDTO;
import com.project.shopapp.entity.Category;
import com.project.shopapp.entity.Product;
import com.project.shopapp.entity.ProductSize;
import com.project.shopapp.response.ApiResponse;
import com.project.shopapp.response.ProductResponse;
import com.project.shopapp.service.ProductSizeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products/sizes")
@RequiredArgsConstructor
public class ProductSizeController {
    private final ProductSizeService productSizeService;

    @PostMapping("/{id}")
    ResponseEntity<?> createProductSize(@PathVariable long id, @RequestBody @Valid ListProductSizeDTO sizes){
        List<ProductSize> productSizes = productSizeService.createProductSize(id,sizes);

        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .payload(productSizes)
                .build());
    }

    @GetMapping("")
    ResponseEntity<ApiResponse<?>> getAllProductSize(){

        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .payload(productSizeService.getAllProductSize())
                .build());
    }

//    @GetMapping("/{id}")
//    ResponseEntity<ApiResponse<?>> getProductSizeById (@PathVariable("id") long Id){
//
//        return ResponseEntity.ok(ApiResponse.builder()
//                .success(true)
//                .payload(productSizeService.getProductSizeById(Id))
//                .build());
//    }

    @GetMapping("/{productId}")
    ResponseEntity<ApiResponse<?>> getProductSizeByProductId (@PathVariable long productId){

        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .payload(productSizeService.getProductSizeByProductId(productId))
                .build());
    }

    @PutMapping("/{id}")
    ResponseEntity<?> updateProductSize(@PathVariable long id, @RequestBody ProductSizeDTO productSizeDTO){
        ProductSize productSize = productSizeService.updateProductSize(id,productSizeDTO);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .payload(productSize)
                .build());
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteProductSize (@PathVariable long id){
        productSizeService.deleteProductSize(id);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Size of product deleted")
                .build());
    }
}
