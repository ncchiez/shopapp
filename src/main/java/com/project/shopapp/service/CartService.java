package com.project.shopapp.service;

import com.project.shopapp.dto.CartItemDTO;
import com.project.shopapp.entity.*;
import com.project.shopapp.exception.AppException;
import com.project.shopapp.exception.ErrorCode;
import com.project.shopapp.repository.*;
import com.project.shopapp.response.CartItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductSizeRepository productSizeRepository;

    public CartItemResponse addToCart(CartItemDTO cartItemDTO) {
        // Lấy User từ userId (nếu cần thêm thông tin)
        User user = userRepository.findByEmail(getEmailFromAuthentication())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Tìm sản phẩm trong CartItem hoặc tạo mới
        Product product = productRepository.findById(cartItemDTO.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        ProductSize productSize = productSizeRepository.findByProductIdAndSize(cartItemDTO.getProductId(), cartItemDTO.getSize())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_SIZE_NOT_EXIST));
        if (productSize.getNumberOfSizes() < cartItemDTO.getQuantity())
            throw new AppException(ErrorCode.INSUFFICIENT_QUANTITY);

        // Lấy giỏ hàng hoặc tạo mới
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseGet(() -> cartRepository.save(Cart.builder().user(user).build()));

        CartItem cartItem = cartItemRepository.findByCartAndProductAndSize(cart, product, cartItemDTO.getSize())
                .orElse(CartItem.builder()
                        .cart(cart)
                        .product(product)
                        .size(cartItemDTO.getSize())
                        .quantity(0)
                        .totalPrice((double) 0)
                        .build());

        // Cập nhật số lượng
        cartItem.setQuantity(cartItem.getQuantity() + cartItemDTO.getQuantity());
        cartItem.setTotalPrice(cartItem.getTotalPrice() + (cartItemDTO.getQuantity() * product.getPrice()));
//        productSize.setNumberOfSizes(productSize.getNumberOfSizes() - cartItemDTO.getQuantity());
//        productSizeRepository.save(productSize);
        cartItemRepository.save(cartItem);
        return cartItem.toCartItemResponse(cartItem);
    }

    public CartItemResponse getCartItemById(Long cartItemId){
        Cart cart = cartRepository.findByUserId(getUserByEmail().getId())
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_EXIST));
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(() -> new AppException(ErrorCode.CART_ITEM_NOT_FOUND));
        return cartItem.toCartItemResponse(cartItem);
    }

    // Lấy danh sách sản phẩm trong giỏ hàng
    public List<CartItemResponse> getCartItems() {

        Cart cart = cartRepository.findByUserId(getUserByEmail().getId())
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_EXIST));

        List<CartItemResponse> cartItemResponses = new ArrayList<CartItemResponse>();
        List<CartItem> cartItems = cartItemRepository.findByCart(cart);
        for (CartItem cartItem: cartItems){
            cartItemResponses.add(cartItem.toCartItemResponse(cartItem));
        }

        return cartItemResponses;
    }

    // Xóa tất cả sản phẩm trong giỏ hàng
    public void clearCart() {
        Cart cart = cartRepository.findByUserId(getUserByEmail().getId())
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_EXIST));

        cartItemRepository.deleteByCart(cart);
    }

    // Xóa một sản phẩm cụ thể khỏi giỏ hàng
    public void removeItemFromCart(Long productId, Integer size) {
        Cart cart = cartRepository.findByUserId(getUserByEmail().getId())
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_EXIST));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        CartItem cartItem = cartItemRepository.findByCartAndProductAndSize(cart, product, size)
                .orElseThrow(() -> new AppException(ErrorCode.CART_ITEM_NOT_FOUND));

        cartItemRepository.delete(cartItem);
    }

    public void removeItemFromCartById(Long cartItemId) {
        Cart cart = cartRepository.findByUserId(getUserByEmail().getId())
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_EXIST));

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_ITEM_NOT_FOUND));
        cartItemRepository.delete(cartItem);
    }

    public CartItemResponse updateCartItem(Long cartItemId, CartItemDTO cartItemDTO) {
        Cart cart = cartRepository.findByUserId(getUserByEmail().getId())
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_EXIST));

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_ITEM_NOT_FOUND));

        Product product = productRepository.findById(cartItemDTO.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        ProductSize productSize = productSizeRepository.findByProductIdAndSize(cartItemDTO.getProductId(), cartItemDTO.getSize())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_SIZE_NOT_EXIST));
        if (productSize.getNumberOfSizes() < cartItemDTO.getQuantity())
            throw new AppException(ErrorCode.INSUFFICIENT_QUANTITY);
        cartItem.setSize(cartItemDTO.getSize());
        cartItem.setQuantity(cartItemDTO.getQuantity());
        cartItem.setTotalPrice(cartItemDTO.getQuantity() * product.getPrice());
        return cartItemRepository.save(cartItem).toCartItemResponse(cartItem);
    }

    private User getUserByEmail(){
        User user = userRepository.findByEmail(getEmailFromAuthentication())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return user;
    }


    private String getEmailFromAuthentication() {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();
        return email;
    }
}
