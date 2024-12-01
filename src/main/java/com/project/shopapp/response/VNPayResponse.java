package com.project.shopapp.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VNPayResponse {
    private String code;
    private String message;
    private BigDecimal amount;
    private String bankTranNo;
    private LocalDateTime payDate;

}
