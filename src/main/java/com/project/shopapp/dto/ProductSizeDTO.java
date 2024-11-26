package com.project.shopapp.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductSizeDTO {
    private Integer size;

    @JsonProperty("number_of_sizes")
    private Integer numberOfSizes;

//    @JsonProperty("product_id")
//    private Long productId;
}
