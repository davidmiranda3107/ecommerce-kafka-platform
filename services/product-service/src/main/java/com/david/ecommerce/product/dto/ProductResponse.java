package com.david.ecommerce.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductResponse {

    private Long id;
    private String name;
    private String description;
    private Double price;
    private String sku;
    private Long categoryId;
}
