package com.david.ecommerce.inventory.service;

import com.david.ecommerce.inventory.domain.dto.InventoryRequest;
import com.david.ecommerce.inventory.domain.dto.InventoryResponse;

public interface InventoryService {

    InventoryResponse getInventoryByProduct(Long productId);

    void increaseStock(InventoryRequest request);

    void decreaseStock(InventoryRequest request);

    int getAvailableStock(Long productId);

    boolean validateStockAvailability(Long productId, int requestedQuantity);

    InventoryResponse reserveStock(InventoryRequest request);

    InventoryResponse releaseStock(InventoryRequest request);

    InventoryResponse createInventory(InventoryRequest request);

}
