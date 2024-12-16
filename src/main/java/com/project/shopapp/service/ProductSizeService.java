package com.project.shopapp.service;


import com.project.shopapp.dto.CategoryDTO;
import com.project.shopapp.dto.ListProductSizeDTO;
import com.project.shopapp.dto.ProductSizeDTO;
import com.project.shopapp.entity.Category;
import com.project.shopapp.entity.OrderDetail;
import com.project.shopapp.entity.Product;
import com.project.shopapp.entity.ProductSize;
import com.project.shopapp.exception.AppException;
import com.project.shopapp.exception.ErrorCode;
import com.project.shopapp.repository.ProductRepository;
import com.project.shopapp.repository.ProductSizeRepository;
import com.project.shopapp.response.OrderDetailResponse;
import com.project.shopapp.response.ProductSizeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductSizeService {
    private final ProductSizeRepository productSizeRepository;
    private final ProductRepository productRepository;


    public List<ProductSize> createProductSize(Long productId, ListProductSizeDTO sizes) {
        // Tìm sản phẩm theo ID
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        for (ProductSizeDTO productSizeDTO : sizes.getSizes()) {
            // Kiểm tra nếu kích thước đã tồn tại cho sản phẩm
            boolean sizeExists = productSizeRepository.existsByProductIdAndSize(existingProduct.getId(), productSizeDTO.getSize());
            if (sizeExists) {
                throw new AppException(ErrorCode.valueOf("size "+ sizeExists + " đã tồn tại!"));
            }
        }

        // Khởi tạo danh sách lưu trữ các ProductSize được tạo
        List<ProductSize> newSizes = new ArrayList<>();

        // Duyệt qua từng ProductSizeDTO
        for (ProductSizeDTO productSizeDTO : sizes.getSizes()) {
            // Tạo đối tượng ProductSize và gắn với sản phẩm
            ProductSize newSize = ProductSize.builder()
                    .size(productSizeDTO.getSize())
                    .numberOfSizes(productSizeDTO.getNumberOfSizes())
                    .product(existingProduct)
                    .build();

            // Thêm vào danh sách
            newSizes.add(newSize);

            // Lưu vào repository ngay trong vòng lặp
            productSizeRepository.save(newSize);
        }

        // Trả về danh sách các ProductSize đã được tạo
        return newSizes;
    }

    public List<ProductSizeResponse> getAllProductSize(){
        List<ProductSize> productSizes = productSizeRepository.findAll();

        List<ProductSizeResponse> list = new ArrayList<>();

        for (ProductSize productSize : productSizes){
            ProductSizeResponse productSizeResponse = new ProductSizeResponse();
            productSizeResponse.setId(productSize.getId());
            productSizeResponse.setSize(productSize.getSize());
            productSizeResponse.setNumberOfSizes(productSize.getNumberOfSizes());
            productSizeResponse.setProductId(productSize.getProduct().getId());

            list.add(productSizeResponse);
        }

        return list;
    }

    public ProductSize getProductSizeById(Long id){
        return productSizeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_SIZE_NOT_EXIST));
    }

    public List<ProductSize> getProductSizeByProductId(Long productId){
        return productSizeRepository.findByProductId(productId);
    }

    public ProductSize updateProductSize(Long id, ProductSizeDTO productSizeDTO) {
        ProductSize existProductSize = getProductSizeById(id);
        existProductSize.setSize(productSizeDTO.getSize());
        existProductSize.setNumberOfSizes(productSizeDTO.getNumberOfSizes());

        return productSizeRepository.save(existProductSize);
    }

    public void deleteProductSize(long id) {
        ProductSize existingProductSize = getProductSizeById(id);
        productSizeRepository.deleteById(id);
    }
}
