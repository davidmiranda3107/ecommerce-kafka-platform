package com.david.ecommerce.product.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryEvent {

    private Long categoryId;
    private String name;
    private String eventType; // CREATED, UPDATED, DELETED

}
