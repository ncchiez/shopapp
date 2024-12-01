package com.project.shopapp.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailResponse {
    private Long id;
//
//    @JsonProperty("order_id")
//    private Long orderId;
//
//    @JsonProperty("product_id")
//    private Long productId;

    private String thumbnail;
    @JsonProperty("product_id")
    private Product product;
    private Integer size;
    @JsonProperty("unit_price")
    private Double unitPrice;
    private Integer quantity;
    @JsonProperty("total_rice")
    private Double totalPrice;

//    private Double price;

//    @JsonProperty("number_of_products")
//    private Integer numberOfProducts;
//
//    @JsonProperty("total_money")
//    private Float totalMoney;
//
//    private Integer size;
}
