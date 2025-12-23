package com.david.ecommerce.order.service;

import com.david.ecommerce.order.dto.OrderItemRequest;
import com.david.ecommerce.order.dto.OrderRequest;
import com.david.ecommerce.order.dto.OrderResponse;
import com.david.ecommerce.order.entities.Order;
import com.david.ecommerce.order.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void shouldCreateOrderSuccessfully() {
        OrderRequest request = new OrderRequest();
        request.setUserId(1L);

        OrderItemRequest itemRequest = new OrderItemRequest();
        itemRequest.setPrice(10d);
        itemRequest.setQuantity(1);
        itemRequest.setProductId(1L);
        request.getItems().add(itemRequest);

        OrderResponse order = orderService.createOrder(request);

        Assertions.assertNotNull(order.getId());
        Assertions.assertEquals(1, order.getItems().size());

    }

    @Test
    void shouldFindOrder() {
        Order order = orderRepository.save(Order.builder()
                .userId(1L)
                .totalAmount(BigDecimal.TEN)
                .build());

        OrderResponse found = orderService.getOrderById(order.getId());

        Assertions.assertEquals(order.getId(), found.getId());
    }
}
