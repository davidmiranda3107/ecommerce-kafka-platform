package com.david.ecommerce.product.kafka;

import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CategoryEventProducer {

    private static final String TOPIC = "category-events";
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendCategoryEvent(CategoryEvent event) {
        kafkaTemplate.send(TOPIC, event.getCategoryId().toString(), event);
    }
}
