package com.david.ecommerce.payment.kafka.consumer;

import com.david.ecommerce.payment.kafka.event.OrderCreatedEvent;
import com.david.ecommerce.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderEventConsumer {

    private final PaymentService paymentService;

    @KafkaListener(topics = "order-created", groupId = "payment-service")
    public void consumeOrderCreated(OrderCreatedEvent event) {
        paymentService.processPayment(event);
    }
}
