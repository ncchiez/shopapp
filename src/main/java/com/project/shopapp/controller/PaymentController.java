package com.project.shopapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.shopapp.dto.OrderDTO;
import com.project.shopapp.response.ApiResponse;
import com.project.shopapp.response.OrderConfirmResponse;
import com.project.shopapp.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentController {

    PaymentService paymentService;

    @PostMapping("/vn-pay")
    public ApiResponse<?> pay(HttpServletRequest request, @RequestBody @Valid OrderDTO orderDTO) throws JsonProcessingException {
        return ApiResponse.builder()
                .success(true)
                .payload(paymentService.createVnPayPayment(request, orderDTO))
                .build();
    }

    @GetMapping("/vn-pay-callback")
    public ApiResponse<?> handleVnPayCallback(HttpServletRequest request, HttpServletResponse response) throws IOException {
        return ApiResponse.builder()
                .success(true)
                .payload(paymentService.getCallBack(request, response))
                .build();
    }
}
