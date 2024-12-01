package com.project.shopapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.shopapp.dto.CartItemDTO;
import com.project.shopapp.dto.OrderDTO;
import com.project.shopapp.entity.Order;
import com.project.shopapp.response.OrderConfirmResponse;
import com.project.shopapp.response.OrderResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IOrderService {
    String createOrderCOD(OrderDTO orderDTO);

    String createOrderVNPay (OrderDTO orderDTO, String userId);

    OrderResponse getOrderById(Long id);

    List<OrderResponse> getAllOrder();

    OrderResponse updateOrder(Long orderId, OrderDTO orderDTO);

    void deleteOrder(Long id);

    List<OrderResponse> findByUserId(String userId);

//    Page<OrderResponse> findByKeyword(String keyword, Pageable pageable);
}
