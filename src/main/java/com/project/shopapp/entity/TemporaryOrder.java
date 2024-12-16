package com.project.shopapp.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
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
@Table(name = "temporary_order")
public class TemporaryOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fullname", length = 100)
    private String fullName;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "phone_number", length = 20, nullable = false)
    private String phoneNumber;

    @Column(name = "shipping_address", length = 200, nullable = false)
    private String shippingAddress;

    @Column(name = "note", length = 100)
    private String note;

    @Column(name = "shipping_method", length = 100)
    private String shippingMethod;

    @Column(name = "payment_method", length = 100)
    private String paymentMethod;

    @Column(name = "product_id")
    private Long productId;

    private String color;
    private Integer size;

    private Integer quantity;

    @Column(name = "is_buy_now")
    private boolean isBuyNow;
}
