package com.david.ecommerce.inventory.service;

import com.david.ecommerce.inventory.domain.dto.InventoryRequest;
import com.david.ecommerce.inventory.domain.entities.Inventory;
import com.david.ecommerce.inventory.domain.enums.InventoryStatus;
import com.david.ecommerce.inventory.repository.InventoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
public class InventoryServiceTest {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Test
    void shouldReserveStockSuccessfully() {
        Inventory inventory = Inventory.builder()
                .productId(1L)
                .availableQuantity(10)
                .status(InventoryStatus.AVAILABLE)
                .build();

        inventoryRepository.save(inventory);

        inventoryService.reserveStock(
            new InventoryRequest(1L, 5)
        );

        Inventory updated = inventoryRepository.findByProductId(1L).orElseThrow();
        assertEquals(5, updated.getAvailableQuantity());
    }
}
