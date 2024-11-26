package com.project.shopapp.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class BrandDTO {
    @NotEmpty(message = "BRAND_NAME_INVALID")
    private String name;
}
