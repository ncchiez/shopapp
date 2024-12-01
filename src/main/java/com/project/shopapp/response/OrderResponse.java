package com.project.shopapp.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.entity.Order;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse{
//    private Long id;
//
//    @JsonProperty("user_id")
//    private String userId;

    @JsonProperty("fullname")
    private String fullName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("shipping_address")
    private String shippingAddress;

    @JsonProperty("order_details")
    private List<OrderDetailResponse> orderDetailResponses;

    @JsonProperty("note")
    private String note;

    @JsonProperty("order_date")
    private Date orderDate;

    @JsonProperty("status")
    private String status;

    @JsonProperty("shipping_method")
    private String shippingMethod;

    @JsonProperty("shipping_cost")
    private Double shippingCost;

    @JsonProperty("shipping_date")
    private LocalDate shippingDate;

    @JsonProperty("payment_method")
    private String paymentMethod;

    @JsonProperty("payment_status")
    private String paymentStatus;

    @JsonProperty("total_money")
    private Float totalMoney;



//    @Column(name = "active")
//    private Boolean active;



//    public static OrderResponse fromOrder(Order order) {
//        return OrderResponse.builder()
//                .id(order.getId())
//                .userId(order.getId())
//                .fullName(order.getFullName())
//                .phoneNumber(order.getPhoneNumber())
//                .shippingAddress(order.getShippingAddress())
//                .email(order.getEmail())
//                .note(order.getNote())
//                .orderDate(order.getOrderDate())
//                .status(order.getStatus())
//                .totalMoney(order.getTotalMoney())
//                .shippingMethod(order.getShippingMethod())
//                .shippingDate(order.getShippingDate())
//                .paymentMethod(order.getPaymentMethod())
//                .active(order.isActive())
////                .orderDetails(OrderDetailResponse.fromOrderDetailList(
////                        order.getOrderDetails()
////                ))
//                .build();
//    }
//
//    public static List<OrderResponse> fromOrdersList(List<Order> ordersList) {
//        return ordersList.stream().map(OrderResponse::fromOrder).toList();
//    }
}
