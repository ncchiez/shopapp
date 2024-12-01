package com.project.shopapp.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.function.DoubleUnaryOperator;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemResponse {
    private Long id;
    private String thumbnail;
    @JsonProperty("product_id")
    private Product product;
    private Integer size;
    @JsonProperty("unit_price")
    private Double unitPrice;
    private Integer quantity;
    @JsonProperty("total_rice")
    private Double totalPrice;

}
