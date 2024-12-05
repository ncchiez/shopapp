package com.project.shopapp.service;


import com.project.shopapp.dto.ProductDTO;
import com.project.shopapp.dto.ProductImageDTO;
import com.project.shopapp.entity.Product;
import com.project.shopapp.entity.ProductImage;
import com.project.shopapp.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface IProductService {
    Product createProduct(ProductDTO productDTO);
    Product getProductById(long productId);
    Page<ProductResponse> getAllProducts(PageRequest pageRequest);
    Product updateProduct(long productId, ProductDTO productDTO);
    void deleteProduct(long productId);
    public ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO);
    Page<ProductResponse> getProductsByBrandId(Long brandId, int page, int limit);
    Page<ProductResponse> getProductsByCategoryIdAndBrandId(Long categoryId, Long brandId, PageRequest pageRequest);
    Page<ProductResponse> searchProductsByName(String name, PageRequest pageRequest);
}
