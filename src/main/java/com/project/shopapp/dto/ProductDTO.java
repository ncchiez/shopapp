package com.project.shopapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {
    @NotBlank(message = "PRODUCT_NAME_INVALID")
    private String name;

    @Min(value = 0, message = "PRODUCT_PRICE_INVALID")
    private Double price;

    private String thumbnail;

    private String description;

    @JsonProperty("category_id")
    private long categoryId;

    @JsonProperty("brand_id")
    private long brandId;

    private List<String> colors;

    @JsonProperty("is_sale")
    private boolean isSale;
}
