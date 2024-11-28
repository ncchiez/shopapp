package com.project.shopapp.service;

import com.project.shopapp.dto.OrderDTO;
import com.project.shopapp.entity.*;
import com.project.shopapp.exception.AppException;
import com.project.shopapp.exception.ErrorCode;
import com.project.shopapp.mapper.OrderMapper;
import com.project.shopapp.repository.*;
import com.project.shopapp.response.OrderDetailResponse;
import com.project.shopapp.response.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;

    @Override
    public OrderResponse createOrder(OrderDTO orderDTO){

        User user = userRepository.findByEmail(getEmailFromAuthentication())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_EXIST));

        // Lấy danh sách sản phẩm trong giỏ hàng
        List<CartItem> cartItems = cartItemRepository.findByCart(cart);

        if (cartItems.isEmpty()) {
            throw new AppException(ErrorCode.CART_NOT_EXIST);
        }

        Order order = orderMapper.toOrder(orderDTO);

        order.setUser(user);
        order.setOrderDate(new Date());
        order.setStatus(OrderStatus.PENDING);
        order.setShippingCost(30000.0);
        order.setTotalMoney(calculateTotalMoney(cartItems) + 30000.0);
        order.setShippingDate(LocalDate.now().plusDays(3));
//        LocalDate shippingDate = order.getShippingDate() == null ? LocalDate.now().plusDays(3) : order.getShippingDate();
//        if (shippingDate.isBefore(LocalDate.now())) {
//            throw new AppException(ErrorCode.SHIPPING_DATE);
//        }
//        order.setShippingDate(shippingDate); // set thời điểm giao hàng

        order.setActive(true); // trạng thái đơn hàng đã được active
        orderRepository.save(order); // lưu vào database

        List<OrderDetail> orderDetails = cartItems.stream()
                .map(cartItem -> OrderDetail.builder()
                        .order(order)
                        .size(cartItem.getSize())
                        .numberOfProducts(cartItem.getQuantity())
                        .totalMoney(cartItem.getTotalPrice())
                        .product(cartItem.getProduct())
                        .build())
                .collect(Collectors.toList());

        // Lưu danh sách OrderDetail vào cơ sở dữ liệu
        orderDetailRepository.saveAll(orderDetails);

        // Xóa các sản phẩm trong giỏ hàng sau khi tạo đơn hàng
        cartItemRepository.deleteAll(cartItems);

        List<OrderDetailResponse> orderDetailResponses = orderDetails.stream()
                .map(orderDetail -> OrderDetailResponse.builder()
                        .thumbnail(orderDetail.getProduct().getThumbnail())
                        .productName(orderDetail.getProduct().getName())
                        .unitPrice(orderDetail.getProduct().getPrice())
                        .quantity(orderDetail.getNumberOfProducts())
                        .size(orderDetail.getSize())
                        .totalPrice(orderDetail.getTotalMoney())
                        .build())
                .collect(Collectors.toList());
        OrderResponse orderResponse = orderMapper.toOrderResponse(order);
        orderResponse.setOrderDetailResponses(orderDetailResponses);
        return orderResponse;
    }

    private Double calculateTotalMoney(List<CartItem> cartItems) {
        return cartItems.stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
    }

    @Override
    public OrderResponse getOrderById(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_EXISTED));
        OrderResponse orderResponse = orderMapper.toOrderResponse(order);
        List<OrderDetailResponse> orderDetailResponses = orderDetailRepository.findByOrderId(order.getId()).stream()
                .map(orderDetail -> OrderDetailResponse.builder()
                        .thumbnail(orderDetail.getProduct().getThumbnail())
                        .productName(orderDetail.getProduct().getName())
                        .unitPrice(orderDetail.getProduct().getPrice())
                        .quantity(orderDetail.getNumberOfProducts())
                        .size(orderDetail.getSize())
                        .totalPrice(orderDetail.getTotalMoney())
                        .build())
                .collect(Collectors.toList());
        orderResponse.setOrderDetailResponses(orderDetailResponses);
        return orderResponse;
    }

    @Override
    public OrderResponse updateOrder(Long id, OrderDTO orderDTO) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_EXISTED));

        orderMapper.updateOrder(order, orderDTO);
        LocalDate shippingDate = order.getShippingDate() == null ? LocalDate.now().plusDays(3) : order.getShippingDate();
        if (shippingDate.isBefore(LocalDate.now())) {
            throw new AppException(ErrorCode.SHIPPING_DATE);
        }
        order.setShippingDate(shippingDate); // set thời điểm giao hàng
        return orderMapper.toOrderResponse(orderRepository.save(order));
    }

    @Override
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_EXISTED));
        if(order != null)
        {
            order.setActive(false);
            orderRepository.save(order);
        }
    }

    @Override
    public List<OrderResponse> findByUserId(String userId) {
        return orderMapper.toListOrderResponses(orderRepository.findByUserId(userId)) ;
    }

    @Override
    public List<OrderResponse> getAllOrder() {

        List<Order> orders = orderRepository.findAll();
        List<OrderResponse> list = new ArrayList<OrderResponse>();
        for(Order order: orders){
            OrderResponse orderResponse = orderMapper.toOrderResponse(order);
            List<OrderDetailResponse> orderDetailResponses = orderDetailRepository.findByOrderId(order.getId()).stream()
                    .map(orderDetail -> OrderDetailResponse.builder()
                            .thumbnail(orderDetail.getProduct().getThumbnail())
                            .productName(orderDetail.getProduct().getName())
                            .unitPrice(orderDetail.getProduct().getPrice())
                            .quantity(orderDetail.getNumberOfProducts())
                            .size(orderDetail.getSize())
                            .totalPrice(orderDetail.getTotalMoney())
                            .build())
                    .collect(Collectors.toList());
            orderResponse.setOrderDetailResponses(orderDetailResponses);
            list.add(orderResponse);
        }
        return list;
    }

    private String getEmailFromAuthentication() {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();
        return email;
    }
}
