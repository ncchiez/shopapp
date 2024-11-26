package com.project.shopapp.response;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    private String thumbnail;
    @JsonProperty("product_name")
    private String productName;
    private Integer size;
    @JsonProperty("unit_price")
    private Double unitPrice;
    private Integer quantity;
    @JsonProperty("total_rice")
    private Double totalPrice;

}
