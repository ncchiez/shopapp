package com.project.shopapp.repository;

import com.project.shopapp.entity.Cart;
import com.project.shopapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    // Tìm giỏ hàng dựa trên người dùng
    Optional<Cart> findByUserId(String userId);

    // Kiểm tra giỏ hàng có tồn tại với người dùng
    boolean existsByUserId(String userId);
}
