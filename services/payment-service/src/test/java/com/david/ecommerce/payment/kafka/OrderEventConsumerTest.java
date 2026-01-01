package com.david.ecommerce.payment.kafka;

import com.david.ecommerce.payment.kafka.event.OrderCreatedEvent;
import com.david.ecommerce.payment.repository.PaymentRepository;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka(
        partitions = 1,
        topics = { "order-created" }
)
public class OrderEventConsumerTest {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private PaymentRepository paymentRepository;

    @Test
    void shouldConsumeOrderCreatedEvent() {
        OrderCreatedEvent event = new OrderCreatedEvent();
        event.setOrderId(1L);
        event.setUserId(1L);
        event.setTotalAmount(BigDecimal.valueOf(100));
        event.setCurrency("USD");
        event.setMethod("DEBIT_CARD");

        kafkaTemplate.send("order-created", event);

        Awaitility.await()
                .atMost(60, TimeUnit.SECONDS)
                .untilAsserted(() ->
                        assertFalse(paymentRepository.findAll().isEmpty())
                );
    }
}
