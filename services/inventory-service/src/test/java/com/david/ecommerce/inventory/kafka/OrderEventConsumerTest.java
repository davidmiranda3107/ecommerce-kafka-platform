package com.david.ecommerce.inventory.kafka;

import com.david.ecommerce.inventory.domain.entities.Inventory;
import com.david.ecommerce.inventory.kafka.event.OrderCreatedEvent;
import com.david.ecommerce.inventory.repository.InventoryRepository;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.ConfluentKafkaContainer;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class OrderEventConsumerTest {

    @Container @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Container @ServiceConnection
    static ConfluentKafkaContainer kafka = new ConfluentKafkaContainer("confluentinc/cp-kafka:7.6.1");

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private InventoryRepository inventoryRepository;

    @AfterEach
    void tearDown() {
        inventoryRepository.deleteAll();
    }

    @Test
    void shouldConsumeOrder() {
        Inventory inventory = new Inventory();
        inventory.setProductId(1L);
        inventory.setAvailableQuantity(200);

        inventoryRepository.save(inventory);

        OrderCreatedEvent event = new OrderCreatedEvent();
        event.setOrderId(1L);
        event.setProductId(1L);
        event.setQuantity(10);

        kafkaTemplate.send("order-created", event);

        Awaitility.await()
                .atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    Optional<Inventory> reserved = inventoryRepository.findByProductId(1L);
                    Inventory inventoryReserved = reserved.orElseGet(Inventory::new);
                    Assertions.assertEquals(190, inventoryReserved.getAvailableQuantity());
                });

    }
}
