package com.david.ecommerce.order.kafka.consumer;

import com.david.ecommerce.order.entities.Order;
import com.david.ecommerce.order.entities.OrderStatus;
import com.david.ecommerce.order.kafka.event.ProductOutOfStock;
import com.david.ecommerce.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProductEventConsumer {
    private final OrderRepository orderRepository;

    @KafkaListener(topics = "product-out-of-stock", groupId = "order-service")
    public void consume(ProductOutOfStock event) {
        log.info("Product out of stock: {}", event.getProductId());

        List<Order> pendingOrders = orderRepository.findDistinctByStatusAndItems_ProductId(OrderStatus.CREATED, event.getProductId());

        pendingOrders.forEach(order -> {
            order.setStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);
        });
    }
}
