package com.david.ecommerce.product.kafka;

import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ProductEventProducer {

    private static final String TOPIC = "product-events";
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendProductEvent(ProductEvent event) {
        kafkaTemplate.send(TOPIC, event.getProductId().toString(), event);
    }
}
