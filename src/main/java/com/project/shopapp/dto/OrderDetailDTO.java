package com.project.shopapp.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailDTO {
    @JsonProperty("order_id")
    @Min(value = 1, message = "ORDER_ID_REQUIRED")
    private Long orderId;

    @JsonProperty("product_id")
    @Min(value = 1, message = "PRODUCT_ID_REQUIRED")
    private Long productId;

    @Min(value = 0, message = "PRODUCT_PRICE_MIN_REQUIRED")
    private Float price;

    @JsonProperty("number_of_products")
    @Min(value = 1, message = "NUMBER_OF_PRODUCT_REQUIRED")
    private int numberOfProducts;

    @JsonProperty("total_money")
    @Min(value = 0, message = "TOTAL_MONEY_REQUIRED")
    private Float totalMoney;

    private Integer size;
}
