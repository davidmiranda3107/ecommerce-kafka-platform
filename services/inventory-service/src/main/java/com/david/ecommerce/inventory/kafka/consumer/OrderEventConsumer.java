package com.david.ecommerce.inventory.kafka.consumer;

import com.david.ecommerce.inventory.domain.dto.InventoryRequest;
import com.david.ecommerce.inventory.kafka.event.OrderCancelledEvent;
import com.david.ecommerce.inventory.kafka.event.OrderCreatedEvent;
import com.david.ecommerce.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventConsumer {
    private final InventoryService inventoryService;

    @KafkaListener(topics = "order-created", groupId = "inventory-service")
    public void onOrderCreated(OrderCreatedEvent event) {
        inventoryService.reserveStock(
               new InventoryRequest(
                       event.getProductId(),
                       event.getQuantity()
               )
        );
    }

    @KafkaListener(topics = "order-cancelled", groupId = "inventory-service")
    public void onOrderCancelled(OrderCancelledEvent event) {
        inventoryService.releaseStock(
                new InventoryRequest(
                        event.getProductId(),
                        event.getQuantity()
                )
        );
    }
}
