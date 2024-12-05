package com.project.shopapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.shopapp.dto.CartItemDTO;
import com.project.shopapp.dto.OrderDTO;
import com.project.shopapp.entity.Order;
import com.project.shopapp.response.ApiResponse;
import com.project.shopapp.response.OrderConfirmResponse;
import com.project.shopapp.response.OrderDetailResponse;
import com.project.shopapp.response.OrderResponse;
import com.project.shopapp.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     Đặt hàng COD
     **/
    @PostMapping("/checkout")
    ApiResponse createOrder(@RequestBody @Valid OrderDTO orderDTO){

        return ApiResponse.builder()
                .success(true)
                .payload(orderService.createOrderCOD(orderDTO))
                .build();
    }
    /**
     Xác nhận đơn
     **/
    @PostMapping("/order")
    ResponseEntity<?> getOrderConfirm(@RequestBody @Valid CartItemDTO cartItemDTO){
        OrderConfirmResponse orderConfirmResponse = orderService.getOrderConfirm(cartItemDTO);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .payload(orderConfirmResponse)
                .build());
    }

    /**
    lẩy ra đơn hàng theo order_id
    **/
    @GetMapping("/{id}")
    ResponseEntity<?> getOrderById(@Valid @PathVariable Long id){
        OrderResponse orderResponse = orderService.getOrderById(id);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .payload(orderResponse)
                .build());
    }

    /**
     lẩy ra tất cả đơn hàng
     **/
    @GetMapping("")
    ResponseEntity<?> getAllOrder(){
        List<OrderResponse> orders = orderService.getAllOrder();
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .payload(orders)
                .build());
    }

    /**
     * Lấy ra danh sách đơn hàng theo user_id
     **/
    @GetMapping("/user/{user_id}")
    ResponseEntity<?> getOrdersByUserId(@Valid @PathVariable("user_id") String userId){
        List<OrderResponse> orders = orderService.findByUserId(userId);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .payload(orders)
                .build());
    }

    @PutMapping("/{id}")
    ResponseEntity<?> updateOrder(@PathVariable @Valid Long id, @Valid @RequestBody OrderDTO orderDTO){
        OrderResponse orderResponse = orderService.updateOrder(id,orderDTO);

        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .payload(orderResponse)
                .build());
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteOrder(@PathVariable Long id){
        orderService.deleteOrder(id);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .payload("Order deleted"));
    }

}
