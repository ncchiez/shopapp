package com.project.shopapp.service;


import com.project.shopapp.dto.OrderDetailDTO;
import com.project.shopapp.entity.OrderDetail;
import com.project.shopapp.response.OrderDetailResponse;

import java.util.List;

public interface IOrderDetailService {
    OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO);

    OrderDetail getOrderDetailById(Long id);

    OrderDetail updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO);

    void deleteOrderDetail(Long id);

    List<OrderDetail> findByOrderId(Long orderId);
}
