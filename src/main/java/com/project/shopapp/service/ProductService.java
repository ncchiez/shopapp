package com.project.shopapp.service;

import com.project.shopapp.dto.ProductDTO;
import com.project.shopapp.dto.ProductImageDTO;
import com.project.shopapp.dto.ProductSizeDTO;
import com.project.shopapp.entity.*;
import com.project.shopapp.exception.AppException;
import com.project.shopapp.exception.ErrorCode;
import com.project.shopapp.mapper.ProductMapper;
import com.project.shopapp.repository.*;
import com.project.shopapp.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;
    private final ProductSizeRepository productSizeRepository;
    private final ProductColorRepository productColorRepository;
    private final BrandRepository brandRepository;
    private final ProductMapper productMapper;
    private final Double discountPercentage = 40.0;

    @Override
    public Product createProduct(ProductDTO productDTO) {
        Category existingCategory = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));
        Brand existingBrand = brandRepository.findById(productDTO.getBrandId())
                .orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_EXISTED));
        if(productRepository.existsByName(productDTO.getName()))
            throw new AppException(ErrorCode.PRODUCT_EXISTED);

        Product newProduct = Product.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .thumbnail(productDTO.getThumbnail())
                .description(productDTO.getDescription())
                .category(existingCategory)
                .brand(existingBrand)
                .isSale(productDTO.isSale())
                .build();
        if(productDTO.isSale()){
            newProduct.setDiscountedPrice(productDTO.getPrice()* (1 - discountPercentage / 100));
        }

        List<ProductColor> productColors = productDTO.getColors().stream()
                .map(color -> ProductColor.builder()
                        .product(newProduct)
                        .color(color)
                        .build())
                .collect(Collectors.toList());

        newProduct.setColors(productColors);
        return productRepository.save(newProduct);
    }

    public List<ProductColor> getProductColorByProductId(Long productId){
        return productColorRepository.findByProductId(productId);
    }

    public ProductResponse getProductDetail(long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        return ProductResponse.builder()
                .id(productId)
                .name(product.getName())
                .price(product.getPrice())
                .discountedPrice(product.getDiscountedPrice())
                .thumbnail(product.getThumbnail())
                .description(product.getDescription())
                .category(product.getCategory())
                .brandId(product.getBrand().getId())
                .productImages(productImageRepository.findByProductId(productId))
                .sizes(productSizeRepository.findByProductId(productId))
                .colors(productColorRepository.findByProductId(productId))
                .isSale(product.getIsSale())
                .build();
    }

    @Override
    public Product getProductById(long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        return product;
    }


    @Override
    public Page<ProductResponse> getAllProducts(PageRequest pageRequest) {
        return productRepository.findAll(pageRequest).map(ProductResponse::fromProduct);
    }


    public Page<ProductResponse> getProductsSale(PageRequest pageRequest, Boolean isSale) {
        return productRepository.findByIsSale(pageRequest, isSale).map(ProductResponse::fromProduct);
    }

    @Override
    public Page<ProductResponse> searchProductsByName(String name, PageRequest pageRequest) {
        return productRepository.findByNameContainingIgnoreCase(name, pageRequest)
                .map(ProductResponse::fromProduct);
    }

    @Override
    public Product updateProduct(long productId, ProductDTO productDTO) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
        Category existingCategory = categoryRepository.findById(productDTO.getCategoryId())
                        .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));
        Brand existingBrand = brandRepository.findById(productDTO.getBrandId())
                .orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_EXISTED));

        existingProduct.setName(productDTO.getName());
        existingProduct.setCategory(existingCategory);
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setThumbnail(productDTO.getThumbnail());
        existingProduct.setBrand(existingBrand);
        existingProduct.setIsSale(productDTO.isSale());
        if(productDTO.isSale()){
            existingProduct.setDiscountedPrice(productDTO.getPrice()* (1 - discountPercentage / 100));
        }
        List<ProductColor> productColors = productDTO.getColors().stream()
                .map(color -> ProductColor.builder()
                        .product(existingProduct)
                        .color(color)
                        .build())
                .collect(Collectors.toList());
        existingProduct.setColors(productColors);

        return productRepository.save(existingProduct);
    }

    @Override
    public void deleteProduct(long productId) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
        productRepository.deleteById(existingProduct.getId());
    }
    @Override
    public ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO) {
        Product existsProduct = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        ProductImage productImage = ProductImage.builder()
                .product(existsProduct)
                .imageUrl(productImageDTO.getImageUrl())
                .build();

        // không cho insert quá x ảnh cho một sản phẩm
        int size = productImageRepository.findByProductId(productId).size();
        if (size >= ProductImage.MAXIMUM_IMAGES_PER_PRODUCT) {
            throw new AppException(ErrorCode.PRODUCT_IMAGE_INVALID);
        }

        return productImageRepository.save(productImage);
    }

    @Override
    public Page<ProductResponse> getProductsByBrandId(Long brandId, int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        return productRepository.findByBrandId(brandId, pageable).map(ProductResponse::fromProduct);
    }
    @Override
    public Page<ProductResponse> getProductsByCategoryIdAndBrandId(Long categoryId, Long brandId, PageRequest pageRequest) {
        return productRepository.findByCategoryIdAndBrandId(categoryId, brandId, pageRequest).map(ProductResponse::fromProduct);
    }

    public void convertThumbnail(ProductImage productImage, Product product){
        product.setThumbnail(productImage.getImageUrl());
        productRepository.save(product);
    }



}
