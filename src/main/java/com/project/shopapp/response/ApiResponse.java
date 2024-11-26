package com.project.shopapp.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
// Loai bo null
public class ApiResponse<T> {
    private boolean success = false;
    @Builder.Default
    private int code = 200;
    private String message;
    private T payload;
    private List<String> errors;
    private Long id;
}
