package com.project.shopapp.service;

import com.project.shopapp.dto.OrderDTO;
import com.project.shopapp.entity.Order;
import com.project.shopapp.response.OrderResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IOrderService {
    OrderResponse createOrder(OrderDTO orderDTO);

    OrderResponse getOrderById(Long id);

    List<OrderResponse> getAllOrder();

    OrderResponse updateOrder(Long orderId, OrderDTO orderDTO);

    void deleteOrder(Long id);

    List<OrderResponse> findByUserId(String userId);

//    Page<OrderResponse> findByKeyword(String keyword, Pageable pageable);
}
