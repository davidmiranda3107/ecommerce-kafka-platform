package com.david.ecommerce.inventory.exception;

public class InventoryNotFoundException extends RuntimeException {

    public InventoryNotFoundException(String message) {
        super(message);
    }

    public InventoryNotFoundException(Long productId) {
        super ("Inventory not found for product " + productId);
    }
}
