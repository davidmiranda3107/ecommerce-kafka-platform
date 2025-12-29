package com.david.ecommerce.inventory.repository;

import com.david.ecommerce.inventory.domain.entities.InventoryMovement;
import com.david.ecommerce.inventory.domain.enums.InventoryMovementType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Transactional
@Testcontainers
public class InventoryMovementRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private InventoryMovementRepository movementRepository;

    @Test
    void shouldSaveAndFindMovementsByProductId() {
        InventoryMovement movement = InventoryMovement.builder()
                .productId(200L)
                .quantityChange(5)
                .type(InventoryMovementType.RESERVE)
                .createdAt(LocalDateTime.now())
                .build();

        movementRepository.save(movement);

        List<InventoryMovement> movements =
                movementRepository.findByProductIdOrderByCreatedAtDesc(200L);

        assertEquals(1, movements.size());
        assertEquals(InventoryMovementType.RESERVE, movements.getFirst().getType());
    }
}
