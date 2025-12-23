package com.david.ecommerce.order.service.impl;

import com.david.ecommerce.order.dto.OrderItemResponse;
import com.david.ecommerce.order.dto.OrderRequest;
import com.david.ecommerce.order.dto.OrderResponse;
import com.david.ecommerce.order.entities.Order;
import com.david.ecommerce.order.entities.OrderItem;
import com.david.ecommerce.order.entities.OrderStatus;
import com.david.ecommerce.order.kafka.event.OrderCreatedEvent;
import com.david.ecommerce.order.kafka.event.OrderStatusChangedEvent;
import com.david.ecommerce.order.exception.NotFoundException;
import com.david.ecommerce.order.kafka.OrderEventProducer;
import com.david.ecommerce.order.repository.OrderItemRepository;
import com.david.ecommerce.order.repository.OrderRepository;
import com.david.ecommerce.order.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private OrderEventProducer eventProducer;

    @Override
    public OrderResponse createOrder(OrderRequest request) {
        Order order = new Order();
        order.setUserId(request.getUserId());

        List<OrderItem> items = request.getItems().stream()
                .map(itemReq -> {
                    OrderItem item = new OrderItem();
                    item.setProductId(itemReq.getProductId());
                    item.setQuantity(itemReq.getQuantity());
                    item.setPrice(BigDecimal.valueOf(itemReq.getPrice()));
                    item.setOrder(order);
                    return item;
                })
                .toList();

        order.setItems(items);
        order.setTotalAmount(calculateTotal(items));

        Order savedOrder = orderRepository.save(order);

        eventProducer.publishOrderCreated(
                new OrderCreatedEvent(
                        savedOrder.getId(),
                        savedOrder.getUserId(),
                        savedOrder.getTotalAmount()
                )
        );

        return mapToResponse(savedOrder);
    }

    @Override
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found: " + id));

        return mapToResponse(order);
    }

    @Override
    public List<OrderResponse> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found: " + orderId));

        order.setStatus(OrderStatus.CANCELLED);
        Order cancelledOrder = orderRepository.save(order);

        eventProducer.publishStatusChanged(
                new OrderStatusChangedEvent(cancelledOrder.getId(), cancelledOrder.getStatus())
        );
    }

    private BigDecimal calculateTotal(List<OrderItem> items) {
        return items.stream()
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private OrderResponse mapToResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getUserId(),
                order.getStatus().toString(),
                order.getTotalAmount().doubleValue(),
                order.getCreatedAt(),
                order.getItems().stream()
                        .map(this::mapToItemResponse)
                        .toList()
        );
    }

    private OrderItemResponse mapToItemResponse(OrderItem item) {
        return new OrderItemResponse(
                item.getProductId(),
                item.getQuantity(),
                item.getPrice().doubleValue()
        );
    }
}
