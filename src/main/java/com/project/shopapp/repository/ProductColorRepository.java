package com.project.shopapp.repository;

import com.project.shopapp.entity.ProductColor;
import com.project.shopapp.entity.ProductSize;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductColorRepository extends JpaRepository<ProductColor, Integer> {
    List<ProductColor> findByProductId(Long productId);
    Optional<ProductColor> findByProductIdAndColor(Long productId, String color);
}
