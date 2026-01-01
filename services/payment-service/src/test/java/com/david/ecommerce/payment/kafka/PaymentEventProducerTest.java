package com.david.ecommerce.payment.kafka;

import com.david.ecommerce.payment.kafka.event.PaymentCompletedEvent;
import com.david.ecommerce.payment.kafka.event.PaymentFailedEvent;
import com.david.ecommerce.payment.kafka.producer.PaymentEventProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka(
        partitions = 1,
        topics = { "payment-events" }
)
public class PaymentEventProducerTest {

    @Autowired
    private PaymentEventProducer producer;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Test
    void shouldPublishPaymentCompletedEvent() {
        PaymentCompletedEvent event = new PaymentCompletedEvent(
                1L,
                1L,
                BigDecimal.valueOf(100),
                "SUCCESS"
        );

        assertDoesNotThrow(() -> producer.sendPaymentCompletedEvent(event));
    }

    @Test
    void shouldPublishPaymentFailedEvent() {
        PaymentFailedEvent event = new PaymentFailedEvent(
                1L,
                "REJECTED BY GATEWAY"
        );

        assertDoesNotThrow(() -> producer.sendPaymentFailedEvent(event));
    }
}
