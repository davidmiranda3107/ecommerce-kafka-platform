package com.david.ecommerce.payment.kafka.producer;

import com.david.ecommerce.payment.kafka.event.PaymentCompletedEvent;
import com.david.ecommerce.payment.kafka.event.PaymentFailedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendPaymentCompletedEvent(PaymentCompletedEvent event) {
        kafkaTemplate.send("payment-completed-topic", event);
    }

    public void sendPaymentFailedEvent(PaymentFailedEvent event) {
        kafkaTemplate.send("payment-failed-topic", event);
    }
}
