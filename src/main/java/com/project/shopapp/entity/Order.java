package com.project.shopapp.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "fullname", length = 100)
    private String fullName;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "phone_number", length = 20, nullable = false)
    private String phoneNumber;

    @Column(name = "shipping_address", length = 1000, nullable = false)
    private String shippingAddress;

    @Column(name = "note", length = 500)
    private String note;

    @Column(name = "order_date")
    private Date orderDate;

    @Column(name = "status")
    private String status;

    @Column(name = "shipping_cost")
    private Double shippingCost;

    @Column(name = "shipping_method", length = 100)
    private String shippingMethod;

    @Column(name = "shipping_date")
    private LocalDate shippingDate;

//    @Column(name = "tracking_number", length = 100)
//    private String trackingNumber;

    @Column(name = "payment_method", length = 100)
    private String paymentMethod;

    @Column(name = "total_money")
    private Double totalMoney;

    @Column(name = "payment_status")
    private String paymentStatus;

    @Column(name = "active")
    private boolean active; // thuộc quyền admin
}
