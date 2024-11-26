package com.project.shopapp.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.dto.ListProductSizeDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductSizeResponse {
    private Integer size;
    @JsonProperty("number_of_sizes")
    private Integer numberOfSizes;
//    ListProductSizeDTO sizes;
    @JsonProperty("product_id")
    private Long productId;
}
