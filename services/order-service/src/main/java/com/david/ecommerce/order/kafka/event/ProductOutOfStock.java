package com.david.ecommerce.order.kafka.event;

import lombok.Data;

@Data
public class ProductOutOfStock {

    private Long productId;
}
