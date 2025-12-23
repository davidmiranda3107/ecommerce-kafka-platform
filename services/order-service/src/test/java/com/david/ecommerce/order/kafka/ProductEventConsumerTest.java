package com.david.ecommerce.order.kafka;

import com.david.ecommerce.order.entities.Order;
import com.david.ecommerce.order.entities.OrderItem;
import com.david.ecommerce.order.entities.OrderStatus;
import com.david.ecommerce.order.kafka.event.ProductOutOfStock;
import com.david.ecommerce.order.repository.OrderRepository;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = {"product-out-of-stock"})
@Testcontainers
public class ProductEventConsumerTest {


    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private OrderRepository orderRepository;

    @AfterEach
    void tearDown() {
        orderRepository.deleteAll();
    }

    @Test
    void shouldCancelOrdersWhenProductOutOfStock() {
        Order order = new Order();
        OrderItem item = new OrderItem();
        item.setProductId(1L);
        item.setOrder(order);
        order.getItems().add(item);
        order.setStatus(OrderStatus.CREATED);
        Order saved = orderRepository.save(order);

        ProductOutOfStock event = new ProductOutOfStock();
        event.setProductId(1L);

        kafkaTemplate.send("product-out-of-stock", event);

        Awaitility.await()
                .atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    List<Order> orders = orderRepository.findAll();
                    assertTrue(orders.stream()
                            .allMatch(o -> o.getStatus() == OrderStatus.CANCELLED));
                });
    }
}
