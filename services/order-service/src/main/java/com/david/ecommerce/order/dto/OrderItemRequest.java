package com.david.ecommerce.order.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class OrderItemRequest {

    @NotNull
    private Long productId;

    @Positive
    private Integer quantity;

    @Positive
    private Double price;
}
