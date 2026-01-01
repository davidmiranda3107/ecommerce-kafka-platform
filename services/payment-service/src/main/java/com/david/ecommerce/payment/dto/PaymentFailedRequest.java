package com.david.ecommerce.payment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentFailedRequest {

    @NotNull
    private Long paymentId;

    @NotNull
    private String reason;
}
