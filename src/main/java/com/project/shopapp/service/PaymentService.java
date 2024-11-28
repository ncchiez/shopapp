package com.project.shopapp.service;

import com.project.shopapp.configuaration.VNPayConfig;
import com.project.shopapp.dto.PaymentInfo;
import com.project.shopapp.entity.User;
import com.project.shopapp.exception.AppException;
import com.project.shopapp.exception.ErrorCode;
import com.project.shopapp.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentService {
    private final UserRepository userRepository;
    private final VNPayConfig vnPayConfig;

    public String createVnPayPayment(HttpServletRequest request) {
        BigDecimal amount = new BigDecimal(request.getParameter("amount")).multiply(BigDecimal.valueOf(100));
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();

        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        PaymentInfo paymentInfo = new PaymentInfo()
                .setReference(vnPayConfig.getRandomNumber(8))
                .setAmount(amount)
                .setDescription("Thanh toan")
                .setExpiresIn(Duration.ofMinutes(15))
                .setIpAddress(extractIPAddress(request));
        String paymentUrl = vnPayConfig.getPaymentURL(paymentInfo);

        return paymentUrl;
    }

    public static String extractIPAddress(HttpServletRequest request) {
        String ipAdress;
        try {
            ipAdress = request.getHeader("X-FORWARDED-FOR");
            if (ipAdress == null) {
                ipAdress = request.getRemoteAddr();
            }
        } catch (Exception e) {
            ipAdress = "Invalid IP:" + e.getMessage();
        }
        return ipAdress;
    }
}
