package com.project.shopapp.repository;

import com.project.shopapp.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByName(String name);
    Page<Product> findAll(Pageable pageable);
    // Tìm sản phẩm theo tên hãng
    Page<Product> findByBrandId(Long brandId, Pageable pageable);
    // Tìm sản phẩm theo tên hãng và loại
    Page<Product> findByCategoryIdAndBrandId(Long categoryId, Long brandId, Pageable pageable);

    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
