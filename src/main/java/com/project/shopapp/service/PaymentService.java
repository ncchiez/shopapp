package com.project.shopapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.shopapp.configuaration.VNPayConfig;
import com.project.shopapp.dto.CartItemDTO;
import com.project.shopapp.dto.OrderDTO;
import com.project.shopapp.dto.PaymentInfo;
import com.project.shopapp.entity.TemporaryOrder;
import com.project.shopapp.entity.User;
import com.project.shopapp.exception.AppException;
import com.project.shopapp.exception.ErrorCode;
import com.project.shopapp.repository.TemporaryOrderRepository;
import com.project.shopapp.repository.UserRepository;
import com.project.shopapp.response.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentService {
    private final UserRepository userRepository;
    private final OrderService orderService;
    private final TemporaryOrderRepository temporaryOrderRepository;
    private final VNPayConfig vnPayConfig;

    public String createVnPayPayment(HttpServletRequest request, OrderDTO orderDTO) throws JsonProcessingException {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));


        TemporaryOrder temporaryOrder = new TemporaryOrder();
        temporaryOrder.setFullName(orderDTO.getFullName());
        temporaryOrder.setEmail(orderDTO.getEmail());
        temporaryOrder.setPhoneNumber(orderDTO.getPhoneNumber());
        temporaryOrder.setShippingAddress(orderDTO.getShippingAddress());
        temporaryOrder.setShippingMethod(orderDTO.getShippingMethod());
        temporaryOrder.setPaymentMethod(orderDTO.getPaymentMethod());
        temporaryOrder.setNote(orderDTO.getNote());
        temporaryOrder.setProductId(orderDTO.getCartItemDTO().getProductId());
        temporaryOrder.setColor(orderDTO.getCartItemDTO().getColor());
        temporaryOrder.setSize(orderDTO.getCartItemDTO().getSize());
        temporaryOrder.setQuantity(orderDTO.getCartItemDTO().getQuantity());
        temporaryOrder.setBuyNow(orderDTO.getCartItemDTO().isBuyNow());

        temporaryOrderRepository.save(temporaryOrder);

        BigDecimal amount = new BigDecimal(request.getParameter("amount")).multiply(BigDecimal.valueOf(100));

        PaymentInfo paymentInfo = new PaymentInfo()
                .setReference(user.getId() + "_" + vnPayConfig.getRandomNumber(6))
                .setAmount(amount)
                .setDescription("Thanh toan")
                .setExpiresIn(Duration.ofMinutes(15))
                .setIpAddress(extractIPAddress(request));
        String paymentUrl = vnPayConfig.getPaymentURL(paymentInfo);

        return paymentUrl;
    }

    public void getCallBack(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String transactionStatus = request.getParameter("vnp_ResponseCode");
        String redirectUrl;

        BigDecimal amountInVNPay = new BigDecimal(request.getParameter("vnp_Amount"));
        BigDecimal actualAmount = amountInVNPay.divide(new BigDecimal(100));
        Double amount = actualAmount.doubleValue();
        String bankTranNo = request.getParameter("vnp_BankTranNo");
        String payDateString = (request.getParameter("vnp_PayDate"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime payDate = LocalDateTime.parse(payDateString, formatter);
        String paymentRef = request.getParameter("vnp_TxnRef");
        String[] refParts = paymentRef.split("_");
        TemporaryOrder temporaryOrder = temporaryOrderRepository.findAll().getLast();


        if ("00".equals(transactionStatus)) {

            orderService.createOrderVNPay(OrderDTO.builder()
                    .fullName(temporaryOrder.getFullName())
                    .email(temporaryOrder.getEmail())
                    .phoneNumber(temporaryOrder.getPhoneNumber())
                    .shippingAddress(temporaryOrder.getShippingAddress())
                    .note(temporaryOrder.getNote())
                    .shippingMethod(temporaryOrder.getShippingMethod())
                    .paymentMethod(temporaryOrder.getPaymentMethod())
                    .cartItemDTO(new CartItemDTO(temporaryOrder.getProductId(), temporaryOrder.getColor(),temporaryOrder.getSize(),temporaryOrder.getQuantity(),temporaryOrder.isBuyNow()))
                    .build(), refParts[0]);
            redirectUrl = "http://localhost:5173/account/orders?code=00";
        }
        else if ("24".equals(transactionStatus)) {
            temporaryOrderRepository.deleteAll();
            redirectUrl = "http://localhost:5173/account/orders?code=24";
        } else {
            temporaryOrderRepository.deleteAll();
            redirectUrl = "http://localhost:5173/account/orders?code=01";
        }
        response.sendRedirect(redirectUrl);
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
