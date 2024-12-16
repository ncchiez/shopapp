package com.project.shopapp.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.entity.*;
import jakarta.persistence.OneToMany;
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
public class ProductResponse extends BaseResponse{
    private Long id;
    private String name;
    private Double price;
    private String thumbnail;
    private String description;
    @JsonProperty("category")
    private Category category;
    @JsonProperty("brand_id")
    private long brandId;
    @JsonProperty("product_images")
    private List<ProductImage> productImages;

    @JsonProperty("is_sale")
    private Boolean isSale;
    @JsonProperty("discounted_price")
    private Double discountedPrice; // Giá sau giảm

    @JsonProperty("sizes")
    private List<ProductSize> sizes;
    @JsonProperty("colors")
    private List<ProductColor> colors;

    public static ProductResponse fromProduct(Product product) {
        ProductResponse productResponse = ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .thumbnail(product.getThumbnail())
                .description(product.getDescription())
                .category(product.getCategory())
                .brandId(product.getBrand().getId())
                .isSale(product.getIsSale())
                .discountedPrice(product.getDiscountedPrice())
                .build();
        productResponse.setCreatedAt(product.getCreatedAt());
        productResponse.setUpdatedAt(product.getUpdatedAt());
        return productResponse;
    }
}
