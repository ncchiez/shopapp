package com.project.shopapp.repository;

import com.project.shopapp.entity.Product;
import com.project.shopapp.entity.ProductImage;
import com.project.shopapp.entity.ProductSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductSizeRepository extends JpaRepository<ProductSize, Long> {
    List<ProductSize> findByProductId(Long productId);
    Boolean existsByProductIdAndSize(Long productId, Integer size);
    Optional<ProductSize> findByProductIdAndSize(Long productId, Integer size);
}
