package com.project.shopapp.mapper;

import com.project.shopapp.dto.OrderDTO;
import com.project.shopapp.entity.Order;
import com.project.shopapp.response.OrderResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "id", ignore = true) // ko map id
    Order toOrder(OrderDTO orderDTO);

    OrderResponse toOrderResponse(Order order);

    //    @Mapping(target = "order_detail", ignore = true)
//    @Mapping(target = "payment_method", ignore = true)
    void updateOrder(@MappingTarget Order order, OrderDTO orderDTO);

    List<OrderResponse> toListOrderResponses(List<Order> orders);
}
