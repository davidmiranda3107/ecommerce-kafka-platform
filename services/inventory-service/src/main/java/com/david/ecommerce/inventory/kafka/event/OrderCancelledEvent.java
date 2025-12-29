package com.david.ecommerce.inventory.kafka.event;

import lombok.Data;

@Data
public class OrderCancelledEvent {
    private Long orderId;
    private Long productId;
    private Integer quantity;
}
