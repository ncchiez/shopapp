//package com.project.shopapp.controller;
//
//
//import com.project.shopapp.dto.OrderDTO;
//import com.project.shopapp.dto.OrderDetailDTO;
//import com.project.shopapp.response.ApiResponse;
//import com.project.shopapp.response.OrderDetailResponse;
//import com.project.shopapp.response.OrderResponse;
//import com.project.shopapp.service.OrderDetailService;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RequiredArgsConstructor
//@RestController
//@RequestMapping("/order_details")
//public class OrderDetailController {
//
//    private final OrderDetailService orderDetailService;
//
//    @PostMapping("")
//    ResponseEntity<?> createOrderDetail(@RequestBody @Valid OrderDetailDTO orderDetailDTO){
//        OrderDetailResponse orderDetailResponse = orderDetailService.createOrderDetail(orderDetailDTO);
//
//        return ResponseEntity.ok(ApiResponse.builder()
//                .success(true)
//                .payload(orderDetailResponse)
//                .build());
//    }
//
//    @GetMapping("/{id}")
//    ResponseEntity<?> getOrderById(@Valid @PathVariable Long id){
//        OrderDetailResponse orderDetailResponse = orderDetailService.getOrderDetailById(id);
//        return ResponseEntity.ok(ApiResponse.builder()
//                .success(true)
//                .payload(orderDetailResponse)
//                .build());
//    }
//
//    @GetMapping("/order/{order_id}")
//    ResponseEntity<?> getOrderDetailsByOrderId(@Valid @PathVariable("order_id") Long orderId){
//        List<OrderDetailResponse> orderDetailResponses = orderDetailService.findByOrderId(orderId);
//        return ResponseEntity.ok(ApiResponse.builder()
//                .success(true)
//                .payload(orderDetailResponses)
//                .build());
//    }
//
//    @PutMapping("/{id}")
//    ResponseEntity<?> updateOrderDetail(@PathVariable @Valid Long id, @Valid @RequestBody OrderDetailDTO orderDetailDTO){
//        OrderDetailResponse orderDetailResponse = orderDetailService.updateOrderDetail(id,orderDetailDTO);
//
//        return ResponseEntity.ok(ApiResponse.builder()
//                .success(true)
//                .payload(orderDetailResponse)
//                .build());
//    }
//
//    @DeleteMapping("/{id}")
//    ResponseEntity<?> deleteOrderDetail(@Valid @PathVariable Long id){
//        orderDetailService.deleteOrderDetail(id);
//        return ResponseEntity.ok(ApiResponse.builder()
//                .success(true)
//                .payload("Order detail deleted"));
//    }
//}
