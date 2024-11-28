package com.project.shopapp.controller;

import com.project.shopapp.response.ApiResponse;
import com.project.shopapp.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentController {

    PaymentService paymentService;

    @GetMapping("/vn-pay")
    public ApiResponse<?> pay(HttpServletRequest request) {
        return ApiResponse.builder()
                .success(true)
                .payload(paymentService.createVnPayPayment(request))
                .build();
    }
}
