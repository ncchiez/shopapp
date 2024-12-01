package com.project.shopapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.response.OrderConfirmResponse;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDTO {
//    @JsonProperty("user_id")
//    @Min(value = 1, message = "USER_ID_INVALID")
//    private String userId;

    @JsonProperty("fullname")
    private String fullName;

    private String email;

    @JsonProperty("phone_number")
    @NotBlank(message = "PHONE_NUMBER_IS_NULL")
    @Size(min = 10,max= 10, message = "PHONE_NUMBER_SIZE_INVALID")
    private String phoneNumber;

    @JsonProperty("shipping_address")
    @NotBlank(message = "SHIPPING_ADDRESS_IS_NULL")
    private String shippingAddress;

    private String note;

//    @JsonProperty("total_money")
//    @Min(value = 0, message = "TOTAL_MONEY_INVALID")
//    private Float totalMoney;

    @JsonProperty("shipping_method")
    private String shippingMethod;

//    @JsonProperty("shipping_date")
//    private LocalDate shippingDate;

    @JsonProperty("payment_method")
    private String paymentMethod;

    @JsonProperty("order_item")
    private CartItemDTO cartItemDTO;

}
