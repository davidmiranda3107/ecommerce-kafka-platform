package com.david.ecommerce.inventory.domain.dto;

import com.david.ecommerce.inventory.domain.enums.InventoryStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InventoryResponse {

    private Long productId;
    private Integer availableQuantity;
    private InventoryStatus status;
}
