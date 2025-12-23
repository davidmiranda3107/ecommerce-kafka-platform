package com.david.ecommerce.order.kafka;

import com.david.ecommerce.order.kafka.event.OrderCreatedEvent;
import com.david.ecommerce.order.kafka.event.OrderStatusChangedEvent;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishOrderCreated(OrderCreatedEvent event) {
        kafkaTemplate.send("order-created-topic", event);
    }

    public void publishStatusChanged(OrderStatusChangedEvent event) {
        kafkaTemplate.send("order-status-topic", event);
    }
}
