package com.david.ecommerce.notification.kafka.event;

import lombok.Data;

@Data
public class PaymentFailedEvent {
    private Long paymentId;
    private Long orderId;
    private String recipient;
    private String channel;
}
