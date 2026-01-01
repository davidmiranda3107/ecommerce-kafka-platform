package com.david.ecommerce.payment.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentFailedEvent {
    private Long orderId;
    private String reason;
}
