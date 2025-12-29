package com.david.ecommerce.inventory.repository;

import com.david.ecommerce.inventory.domain.entities.Inventory;
import com.david.ecommerce.inventory.domain.enums.InventoryStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@Transactional
@Testcontainers
public class InventoryRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private InventoryRepository inventoryRepository;

    @Test
    void shouldSaveAndFindInventoryByProductId() {
        Inventory inventory = Inventory.builder()
                .productId(100L)
                .availableQuantity(20)
                .status(InventoryStatus.AVAILABLE)
                .build();

        inventoryRepository.save(inventory);

        Inventory found = inventoryRepository.findByProductId(100L).orElse(null);

        assertNotNull(found);
        assertEquals(20, found.getAvailableQuantity());
    }
}
