package com.david.ecommerce.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderItemResponse {

    private Long productId;
    private Integer quantity;
    private Double price;
}
