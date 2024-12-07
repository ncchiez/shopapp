package com.project.shopapp.service;

import com.project.shopapp.dto.CartItemDTO;
import com.project.shopapp.dto.OrderDTO;
import com.project.shopapp.entity.*;
import com.project.shopapp.enums.OrderStatus;
import com.project.shopapp.enums.PaymentStatus;
import com.project.shopapp.exception.AppException;
import com.project.shopapp.exception.ErrorCode;
import com.project.shopapp.mapper.OrderMapper;
import com.project.shopapp.repository.*;
import com.project.shopapp.response.OrderConfirmResponse;
import com.project.shopapp.response.OrderDetailResponse;
import com.project.shopapp.response.OrderResponse;
import lombok.RequiredArgsConstructor;
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
    private final ProductRepository productRepository;
    private final ProductSizeRepository productSizeRepository;
    private final OrderMapper orderMapper;
    private final TemporaryOrderRepository temporaryOrderRepository;

    @Override
    public String createOrderCOD(OrderDTO orderDTO){
        User user = userRepository.findByEmail(getEmailFromAuthentication())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        List<CartItem> cartItems;
        // Mua qua giỏ
        if(!orderDTO.getCartItemDTO().isBuyNow()){
            Cart cart = cartRepository.findByUserId(user.getId())
                    .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_EXIST));

            // Lấy danh sách sản phẩm trong giỏ hàng
            cartItems = cartItemRepository.findByCart(cart);
            if (cartItems.isEmpty()) {
                throw new AppException(ErrorCode.CART_NOT_EXIST);
            }
        // Mua trực tiếp
        }else{
            Product product = productRepository.findById(orderDTO.getCartItemDTO().getProductId())
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

            // Kiểm tra số lượng sản phẩm
            ProductSize productSize = productSizeRepository.findByProductIdAndSize(orderDTO.getCartItemDTO().getProductId(), orderDTO.getCartItemDTO().getSize())
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_SIZE_NOT_EXIST));
            if (productSize.getNumberOfSizes() < orderDTO.getCartItemDTO().getQuantity()) {
                throw new AppException(ErrorCode.INSUFFICIENT_QUANTITY);
            }
            CartItem cartItem = CartItem.builder()
                    .product(product)
                    .quantity(orderDTO.getCartItemDTO().getQuantity())
                    .size(orderDTO.getCartItemDTO().getSize())
                    .totalPrice(product.getPrice() * orderDTO.getCartItemDTO().getQuantity())
                    .build();
            cartItems = List.of(cartItem);
        }
        Order order = orderMapper.toOrder(orderDTO);
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setStatus(OrderStatus.PENDING);
        order.setShippingCost(30000.0);
        order.setTotalMoney(calculateTotalMoney(cartItems) + 30000.0);
        order.setShippingDate(LocalDate.now().plusDays(3));
        order.setPaymentStatus(PaymentStatus.PENDING);
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
        if (!orderDTO.getCartItemDTO().isBuyNow()) {
            cartItemRepository.deleteAll(cartItems);
        }
        return ("Đơn hàng đã được đặt!!");
    }
    @Override
    public String createOrderVNPay (OrderDTO orderDTO, String userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        List<CartItem> cartItems;
        // Mua qua giỏ
        if(!orderDTO.getCartItemDTO().isBuyNow()){
            Cart cart = cartRepository.findByUserId(user.getId())
                    .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_EXIST));

            // Lấy danh sách sản phẩm trong giỏ hàng
            cartItems = cartItemRepository.findByCart(cart);
            if (cartItems.isEmpty()) {
                throw new AppException(ErrorCode.CART_NOT_EXIST);
            }
            // Mua trực tiếp
        }else{
            Product product = productRepository.findById(orderDTO.getCartItemDTO().getProductId())
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

            // Kiểm tra số lượng sản phẩm
            ProductSize productSize = productSizeRepository.findByProductIdAndSize(orderDTO.getCartItemDTO().getProductId(), orderDTO.getCartItemDTO().getSize())
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_SIZE_NOT_EXIST));
            if (productSize.getNumberOfSizes() < orderDTO.getCartItemDTO().getQuantity()) {
                throw new AppException(ErrorCode.INSUFFICIENT_QUANTITY);
            }
            CartItem cartItem = CartItem.builder()
                    .product(product)
                    .quantity(orderDTO.getCartItemDTO().getQuantity())
                    .size(orderDTO.getCartItemDTO().getSize())
                    .totalPrice(product.getPrice() * orderDTO.getCartItemDTO().getQuantity())
                    .build();
            cartItems = List.of(cartItem);
        }
        Order order = orderMapper.toOrder(orderDTO);
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setStatus(OrderStatus.PENDING);
        order.setShippingCost(30000.0);
        order.setTotalMoney(calculateTotalMoney(cartItems) + 30000.0);
        order.setShippingDate(LocalDate.now().plusDays(3));
        order.setPaymentStatus(PaymentStatus.COMPLETED);
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
        if (!orderDTO.getCartItemDTO().isBuyNow()) {
            cartItemRepository.deleteAll(cartItems);
        }
        temporaryOrderRepository.deleteAll();
        return "Thanh toán thành công!!";
    }

    public OrderConfirmResponse getOrderConfirm(CartItemDTO cartItemDTO){
        User user = userRepository.findByEmail(getEmailFromAuthentication())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        if(!cartItemDTO.isBuyNow()){
            Cart cart = cartRepository.findByUserId(user.getId())
                    .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_EXIST));

            // Lấy danh sách sản phẩm trong giỏ hàng
            List<CartItem> cartItems = cartItemRepository.findByCart(cart);
            if (cartItems.isEmpty()) {
                throw new AppException(ErrorCode.CART_NOT_EXIST);
            }
            List<OrderDetail> orderDetails = cartItems.stream()
                    .map(cartItem -> OrderDetail.builder()
                            .size(cartItem.getSize())
                            .numberOfProducts(cartItem.getQuantity())
                            .totalMoney(cartItem.getTotalPrice())
                            .product(cartItem.getProduct())
                            .build())
                    .collect(Collectors.toList());
            List<OrderDetailResponse> orderDetailResponses = orderDetails.stream()
                    .map(orderDetail -> OrderDetailResponse.builder()
                            .thumbnail(orderDetail.getProduct().getThumbnail())
                            .product(orderDetail.getProduct())
                            .unitPrice(orderDetail.getProduct().getPrice())
                            .quantity(orderDetail.getNumberOfProducts())
                            .size(orderDetail.getSize())
                            .totalPrice(orderDetail.getTotalMoney())
                            .build())
                    .collect(Collectors.toList());
            return OrderConfirmResponse.builder()
                    .orderDetailResponses(orderDetailResponses)
                    .shippingCost(30000.0)
                    .totalMoney(calculateTotalMoney(cartItems) + 30000.0)
                    .build();
        }else{
            Product product = productRepository.findById(cartItemDTO.getProductId())
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

            ProductSize productSize = productSizeRepository.findByProductIdAndSize(cartItemDTO.getProductId(), cartItemDTO.getSize())
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_SIZE_NOT_EXIST));
            if (productSize.getNumberOfSizes() < cartItemDTO.getQuantity())
                throw new AppException(ErrorCode.INSUFFICIENT_QUANTITY);

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setProduct(product);
            orderDetail.setNumberOfProducts(cartItemDTO.getQuantity());
            orderDetail.setSize(cartItemDTO.getSize());
            orderDetail.setTotalMoney(product.getPrice() * cartItemDTO.getQuantity());

            OrderDetailResponse orderDetailResponse = new OrderDetailResponse();
            orderDetailResponse.setThumbnail(orderDetail.getProduct().getThumbnail());
            orderDetailResponse.setProduct(orderDetail.getProduct());
            orderDetailResponse.setUnitPrice(orderDetail.getProduct().getPrice());
            orderDetailResponse.setQuantity(orderDetail.getNumberOfProducts());
            orderDetailResponse.setSize(orderDetail.getSize());
            orderDetailResponse.setTotalPrice(orderDetail.getTotalMoney());

            List<OrderDetailResponse> orderDetailResponses;
            orderDetailResponses= List.of(orderDetailResponse);

            return OrderConfirmResponse.builder()
                    .orderDetailResponses(orderDetailResponses)
                    .shippingCost(30000.0)
                    .totalMoney(orderDetailResponse.getTotalPrice() + 30000.0)
                    .isBuyNow(true)
                    .build();
        }
    }

    private Double calculateTotalMoney(List<CartItem> cartItems) {
        return cartItems.stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
    }

    public String updateStatusOrder(Long orderId, String status){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_EXISTED));
        if(getEmailFromAuthentication().equals("admin")){
            if(status.equals("CANCELED")){
                order.setStatus(OrderStatus.CANCELED);
                order.setActive(false);
            }else if(status.equals("DELIVERING"))
                order.setStatus(OrderStatus.DELIVERING);
            else if(status.equals("SHIPPED"))
                order.setStatus(OrderStatus.SHIPPED);
            orderRepository.save(order);
            return "Cập nhật trạng thái " + status +  " cho đơn hàng thành công";
        }
        order.setStatus(OrderStatus.CANCELED);
        order.setActive(false);
        orderRepository.save(order);
        return "Hủy đơn hàng thành công";
    }

    @Override
    public OrderResponse getOrderById(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_EXISTED));
        OrderResponse orderResponse = orderMapper.toOrderResponse(order);
        List<OrderDetailResponse> orderDetailResponses = orderDetailRepository.findByOrderId(order.getId()).stream()
                .map(orderDetail -> OrderDetailResponse.builder()
                        .thumbnail(orderDetail.getProduct().getThumbnail())
                        .product(orderDetail.getProduct())
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
                            .id(orderDetail.getId())
                            .thumbnail(orderDetail.getProduct().getThumbnail())
                            .product(orderDetail.getProduct())
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
