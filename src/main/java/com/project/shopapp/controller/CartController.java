package com.project.shopapp.controller;


import com.project.shopapp.dto.CartItemDTO;
import com.project.shopapp.response.ApiResponse;
import com.project.shopapp.response.CartItemResponse;
import com.project.shopapp.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    /**
     Thêm sản phẩm vào giỏ hàng
     **/
    @PostMapping
    public ResponseEntity<ApiResponse<?>> addToCart(@RequestBody CartItemDTO cartItemDTO) {

        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .payload(cartService.addToCart(cartItemDTO))
                .build());
    }

    /**
     Lấy tất cả sản phẩm trong giỏ (Lấy của bản thân người dùng đăng nhập)
     **/
    @GetMapping
    public ResponseEntity<?> getCartItems() {
        List<CartItemResponse> items = cartService.getCartItems();
        return ResponseEntity.ok(items);
    }

    /**
     Lấy 1 sản phẩm trong giỏ bằng id
     **/
    @GetMapping("/{cart_item_id}")
    public ResponseEntity<?> getCartItemById(@PathVariable("cart_item_id") Long cartItemId) {

        return ResponseEntity.ok(cartService.getCartItemById(cartItemId));
    }

    // Xóa tất cả sản phẩm khỏi giỏ hàng
    @DeleteMapping("/clear")
    public ResponseEntity<String> clearCart() {
        cartService.clearCart();
        return ResponseEntity.ok("Cart cleared successfully");
    }

    // Xóa một sản phẩm khỏi giỏ hàng
    @DeleteMapping()
    public ResponseEntity<String> removeItemFromCart(@RequestParam Long productId, @RequestParam Integer size) {
        cartService.removeItemFromCart(productId, size);
        return ResponseEntity.ok("Product removed from cart successfully");
    }


}
