package com.project.shopapp.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.entity.OrderDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderConfirmResponse {
    @JsonProperty("order_detail")
    private List<OrderDetailResponse> orderDetailResponses;
    @JsonProperty("shipping_cost")
    private Double shippingCost;
    @JsonProperty("total_money")
    private Double totalMoney;
    @JsonProperty("is_buy_now")
    private boolean isBuyNow;
}
