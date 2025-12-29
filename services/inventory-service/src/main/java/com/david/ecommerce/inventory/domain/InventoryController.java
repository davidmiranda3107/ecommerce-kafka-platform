package com.david.ecommerce.inventory.domain;

import com.david.ecommerce.inventory.domain.dto.InventoryRequest;
import com.david.ecommerce.inventory.domain.dto.InventoryResponse;
import com.david.ecommerce.inventory.service.InventoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
@AllArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/{productId}")
    public ResponseEntity<InventoryResponse> getStock(@PathVariable Long productId) {
        return ResponseEntity.ok(inventoryService.getInventoryByProduct(productId));
    }

    @PostMapping("/increase")
    public ResponseEntity<Void> increaseStock(@Valid @RequestBody InventoryRequest request) {
        inventoryService.increaseStock(request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/decrease")
    public ResponseEntity<Void> decreaseStock(@Valid @RequestBody InventoryRequest request) {
        inventoryService.decreaseStock(request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping
    public ResponseEntity<InventoryResponse> createInventory(
            @Valid @RequestBody InventoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new InventoryResponse());
    }
}
