package com.project.shopapp.mapper;

import com.project.shopapp.dto.ProductDTO;
import com.project.shopapp.entity.Product;
import com.project.shopapp.response.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "id", ignore = true) // ko map id
    Product toProduct(ProductDTO productDTO);

    ProductResponse toProductResponse(Product product);
    void updateProduct(@MappingTarget Product product, ProductDTO productDTO);
}
