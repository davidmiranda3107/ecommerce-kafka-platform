package com.david.ecommerce.order.service;

import com.david.ecommerce.order.dto.OrderRequest;
import com.david.ecommerce.order.dto.OrderResponse;

import java.util.List;

public interface OrderService {

    OrderResponse createOrder(OrderRequest request);
    OrderResponse getOrderById(Long id);
    List<OrderResponse> getOrdersByUserId(Long userId);
    void cancelOrder(Long orderId);
}
