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
    public ApiResponse<?> getCartItemById(@PathVariable("cart_item_id") Long cartItemId) {

        return ApiResponse.builder()
                .success(true)
                .payload(cartService.getCartItemById(cartItemId))
                .build();
    }

    @PutMapping("/{cart_item_id}")
    public ApiResponse<?> UpdateCartItemById(@PathVariable("cart_item_id") Long cartItemId, @RequestBody CartItemDTO cartItemDTO) {

        return ApiResponse.builder()
                .success(true)
                .payload(cartService.updateCartItem(cartItemId, cartItemDTO))
                .build();
    }

    // Xóa tất cả sản phẩm khỏi giỏ hàng
    @DeleteMapping("/clear")
    public ApiResponse<?> clearCart() {
        cartService.clearCart();
        return ApiResponse.builder()
                .success(true)
                .payload("Cart cleared successfully")
                .build();
    }

    // Xóa một sản phẩm khỏi giỏ hàng
    @DeleteMapping()
    public ApiResponse<?> removeItemFromCartById(@RequestParam Long cartItemId) {
        cartService.removeItemFromCartById(cartItemId);
        return ApiResponse.builder()
                .success(true)
                .payload("Product removed from cart successfully")
                .build();
    }

//    @DeleteMapping()
//    public ApiResponse<?> removeItemFromCart(@RequestParam Long productId, @RequestParam Integer size) {
//        cartService.removeItemFromCart(productId, size);
//        return ApiResponse.builder()
//                .success(true)
//                .payload("Product removed from cart successfully")
//                .build();
//    }

}
