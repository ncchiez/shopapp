package com.project.shopapp.mapper;

import com.project.shopapp.dto.OrderDetailDTO;
import com.project.shopapp.entity.OrderDetail;
import com.project.shopapp.response.OrderDetailResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderDetailMapper {
    @Mapping(target = "id", ignore = true) // ko map id
    OrderDetail toOrderDetail(OrderDetailDTO orderDetailDTO);

    OrderDetailResponse toOrderDetailResponse(OrderDetail orderDetail);
    void updateOrderDetail(@MappingTarget OrderDetail orderDetail, OrderDetailDTO orderDetailDTO);
    List<OrderDetailResponse> toListOrderDetailResponses(List<OrderDetail> orderDetails);
}
