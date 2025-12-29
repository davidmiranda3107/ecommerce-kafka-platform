package com.david.ecommerce.inventory.service.impl;

import com.david.ecommerce.inventory.domain.dto.InventoryRequest;
import com.david.ecommerce.inventory.domain.dto.InventoryResponse;
import com.david.ecommerce.inventory.domain.entities.Inventory;
import com.david.ecommerce.inventory.domain.entities.InventoryMovement;
import com.david.ecommerce.inventory.domain.enums.InventoryMovementType;
import com.david.ecommerce.inventory.domain.enums.InventoryStatus;
import com.david.ecommerce.inventory.exception.InsufficientStockException;
import com.david.ecommerce.inventory.exception.InventoryNotFoundException;
import com.david.ecommerce.inventory.kafka.producer.InventoryEventProducer;
import com.david.ecommerce.inventory.repository.InventoryRepository;
import com.david.ecommerce.inventory.repository.InventoryMovementRepository;
import com.david.ecommerce.inventory.service.InventoryService;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryMovementRepository inventoryMovementRepository;
    private final InventoryEventProducer eventProducer;

    @Override
    public InventoryResponse getInventoryByProduct(Long productId) {
        Inventory item = getInventory(productId);
        return toResponse(item);
    }

    @Override
    public void increaseStock(InventoryRequest request) {
        Inventory item = getInventory(request.getProductId());

        item.setAvailableQuantity(item.getAvailableQuantity() + request.getQuantity());
        inventoryRepository.save(item);

        registerMovement(item.getProductId(), request.getQuantity(), InventoryMovementType.IN);
    }

    @Override
    public void decreaseStock(InventoryRequest request) {
        Inventory item = getInventory(request.getProductId());

        if (item.getAvailableQuantity() < request.getQuantity()) {
            throw new InsufficientStockException(
                    item.getProductId(),
                    request.getQuantity(),
                    item.getAvailableQuantity()
            );
        }

        item.setAvailableQuantity(item.getAvailableQuantity() - request.getQuantity());
        inventoryRepository.save(item);

        registerMovement(item.getProductId(), request.getQuantity(), InventoryMovementType.OUT);
    }

    @Override
    @Transactional(readOnly = true)
    public int getAvailableStock(Long productId) {
        return inventoryRepository.findByProductId(productId)
                .map(Inventory::getAvailableQuantity)
                .orElseThrow(() -> new InventoryNotFoundException(productId));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean validateStockAvailability(Long productId, int requestedQuantity) {
        return inventoryRepository.findByProductId(productId)
                .map(inv -> inv.getAvailableQuantity() >= requestedQuantity)
                .orElse(false);
    }

    @Override
    public InventoryResponse reserveStock(InventoryRequest request) {
        Inventory item = getInventory(request.getProductId());

        if (item.getAvailableQuantity() < request.getQuantity()) {

            item.setStatus(InventoryStatus.INSUFFICIENT);
            inventoryRepository.save(item);
            throw new InsufficientStockException(
                    item.getProductId(),
                    request.getQuantity(),
                    item.getAvailableQuantity());
        }

        item.setAvailableQuantity(item.getAvailableQuantity() - request.getQuantity());
        item.setReservedQuantity(item.getReservedQuantity() + request.getQuantity());
        item.setStatus(
                item.getAvailableQuantity() == 0
                ? InventoryStatus.OUT_OF_STOCK
                : InventoryStatus.RESERVED);

        inventoryRepository.save(item);
        registerMovement(item.getProductId(), request.getQuantity(), InventoryMovementType.RESERVE);

        eventProducer.sendStockReserved(item.getProductId(), request.getQuantity());
        return toResponse(item);
    }

    @Override
    public InventoryResponse releaseStock(InventoryRequest request) {
        Inventory item = getInventory(request.getProductId());
        item.setAvailableQuantity(item.getAvailableQuantity() + request.getQuantity());
        item.setReservedQuantity(item.getReservedQuantity() - request.getQuantity());
        item.setStatus(InventoryStatus.AVAILABLE);

        inventoryRepository.save(item);
        registerMovement(item.getProductId(), request.getQuantity(), InventoryMovementType.RELEASE);

        eventProducer.sendStockReleased(item.getProductId(), request.getQuantity());
        return toResponse(item);
    }

    @Override
    public InventoryResponse createInventory(InventoryRequest request) {
        return new InventoryResponse();
    }

    private void registerMovement(Long productId, int quantity, InventoryMovementType type) {
        InventoryMovement movement = InventoryMovement.builder()
                .productId(productId)
                .quantityChange(quantity)
                .type(type)
                .build();

        inventoryMovementRepository.save(movement);
    }

    private InventoryResponse toResponse(Inventory item) {
        return InventoryResponse.builder()
                .productId(item.getProductId())
                .availableQuantity(item.getAvailableQuantity())
                .build();
    }

    private Inventory getInventory(Long productId) {
        return inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new InventoryNotFoundException(productId));
    }
}
