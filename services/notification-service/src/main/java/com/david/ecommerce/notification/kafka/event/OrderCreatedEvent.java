package com.david.ecommerce.notification.kafka.event;

import lombok.Data;

@Data
public class OrderCreatedEvent {
    private Long orderId;
    private String recipient;
    private String channel;
}
