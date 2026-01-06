package com.david.ecommerce.notification.kafka.event;

import lombok.Data;

@Data
public class OrderCancelledEvent {
    private Long orderId;
    private String recipient;
    private String channel;
}
