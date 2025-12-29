package com.david.ecommerce.inventory.kafka.producer;

import com.david.ecommerce.inventory.kafka.event.StockInsufficientEvent;
import com.david.ecommerce.inventory.kafka.event.StockReleasedEvent;
import com.david.ecommerce.inventory.kafka.event.StockReservedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InventoryEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendStockReserved(Long productId, int quantity) {
        kafkaTemplate.send("stock-reserved",
                new StockReservedEvent(productId, quantity));
    }

    public void sendStockReleased(Long productId, int quantity) {
        kafkaTemplate.send("stock-released",
                new StockReleasedEvent(productId, quantity));
    }

    public void sendStockInsufficient(Long productId) {
        kafkaTemplate.send("stock-insufficient",
                new StockInsufficientEvent(productId));
    }
}
