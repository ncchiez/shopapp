//package com.project.shopapp.service;
//
//import com.project.shopapp.dto.OrderDetailDTO;
//import com.project.shopapp.entity.Order;
//import com.project.shopapp.entity.OrderDetail;
//import com.project.shopapp.entity.Product;
//import com.project.shopapp.exception.AppException;
//import com.project.shopapp.exception.ErrorCode;
//import com.project.shopapp.mapper.OrderDetailMapper;
//import com.project.shopapp.repository.OrderDetailRepository;
//import com.project.shopapp.repository.OrderRepository;
//import com.project.shopapp.repository.ProductRepository;
//import com.project.shopapp.response.OrderDetailResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class OrderDetailService implements IOrderDetailService{
//    private final OrderDetailRepository orderDetailRepository;
//    private final OrderRepository orderRepository;
//    private final ProductRepository productRepository;
//    private final OrderDetailMapper orderDetailMapper;
//
//
//    @Override
//    public OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) {
//
//        Order order = orderRepository.findById(orderDetailDTO.getOrderId())
//                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_EXISTED));
//        Product product = productRepository.findById(orderDetailDTO.getProductId())
//                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
//
//        OrderDetail orderDetail = orderDetailMapper.toOrderDetail(orderDetailDTO);
//        orderDetail.setOrder(order);
//        orderDetail.setProduct(product);
//
//        return orderDetailRepository.save(orderDetail);
//    }
//
//    @Override
//    public OrderDetail getOrderDetailById(Long id) {
//        OrderDetail orderDetail = orderDetailRepository.findById(id)
//                .orElseThrow(() -> new AppException(ErrorCode.ORDER_DETAIL_NOT_EXISTED));
//
//        OrderDetailResponse orderDetailResponse = orderDetailMapper.toOrderDetailResponse(orderDetail);
//
//        return orderDetail;
//    }
//
//    @Override
//    public OrderDetail updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO) {
//        OrderDetail orderDetail = orderDetailRepository.findById(id)
//                .orElseThrow(() -> new AppException(ErrorCode.ORDER_DETAIL_NOT_EXISTED));
//
//        orderDetailMapper.updateOrderDetail(orderDetail, orderDetailDTO);
//
//        OrderDetailResponse orderDetailResponse = orderDetailMapper.toOrderDetailResponse(orderDetailRepository.save(orderDetail));
//
//        return orderDetail;
//    }
//
//    @Override
//    public void deleteOrderDetail(Long id) {
//        OrderDetail orderDetail = orderDetailRepository.findById(id)
//                .orElseThrow(() -> new AppException(ErrorCode.ORDER_DETAIL_NOT_EXISTED));
//        orderDetailRepository.deleteById(id);
//    }
//
//    @Override
//    public List<OrderDetailResponse> findByOrderId(Long orderId) {
//        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(orderId);
//
//        List<OrderDetailResponse> list = new ArrayList<OrderDetailResponse>( orderDetails.size() );
//        for ( OrderDetail orderDetail : orderDetails ) {
//
//            OrderDetailResponse orderDetailResponse = orderDetailMapper.toOrderDetailResponse( orderDetail );
//            orderDetailResponse.setOrderId(orderId);
//            orderDetailResponse.setProductId(orderDetail.getProduct().getId());
//
//            list.add(orderDetailResponse);
//        }
//        return list;
//        //return orderDetailMapper.toListOrderDetailResponses(orderDetailRepository.findByOrderId(orderId));
//    }
//}
