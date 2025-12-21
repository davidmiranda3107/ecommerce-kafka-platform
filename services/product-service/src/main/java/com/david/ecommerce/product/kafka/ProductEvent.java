package com.david.ecommerce.product.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductEvent {

    private Long productId;
    private String name;
    private Double price;
    private String sku;
    private String eventType; // CREATED, UPDATED, DELETED
}
