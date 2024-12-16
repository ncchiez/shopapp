package com.project.shopapp.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.project.shopapp.response.CartItemResponse;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "cart_items")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart; // Liên kết với giỏ hàng

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "product_id", nullable = false)
    private Product product; // Sản phẩm trong giỏ hàng

    @Column(name = "color", nullable = false)
    private String color;

    @Column(name = "size", nullable = false)
    private Integer size; // Kích thước sản phẩm

    @Column(name = "quantity", nullable = false)
    private Integer quantity; // Số lượng sản phẩm

    @Column(name = "total_price", nullable = false)
    private Double totalPrice;

    public CartItemResponse toCartItemResponse(CartItem cartItem){
        Double price;
        if(cartItem.getProduct().getIsSale()){
            price = cartItem.getProduct().getDiscountedPrice();
        }else {
            price = cartItem.getProduct().getPrice();
        }
        return CartItemResponse.builder()
                .id(cartItem.getId())
                .thumbnail(cartItem.getProduct().getThumbnail())
                .product(cartItem.getProduct())
                .color(cartItem.getColor())
                .size(cartItem.getSize())
                .unitPrice(price)
                .quantity(cartItem.getQuantity())
                .totalPrice(cartItem.totalPrice)
                .build();

    }
}
