package com.project.shopapp.repository;

import com.project.shopapp.entity.Cart;
import com.project.shopapp.entity.CartItem;
import com.project.shopapp.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartAndProductAndSize(Cart cart, Product product, Integer size);

    // Lấy danh sách các sản phẩm trong giỏ hàng của người dùng
    List<CartItem> findByCart(Cart cart);

    CartItem findByCartIdAndProductId(Long cartId, Long productId);

    // Xóa tất cả CartItem theo Cart
    void deleteByCart(Cart cart);
}
