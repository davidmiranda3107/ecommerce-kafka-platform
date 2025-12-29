package com.david.ecommerce.inventory.kafka.event;

import lombok.Data;

@Data
public class OrderCreatedEvent {
    private Long orderId;
    private Long productId;
    private Integer quantity;
}
