package com.david.ecommerce.notification.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmailRequest {

    @NotNull
    private String recipient;

    @NotNull
    private String subject;

    @NotNull
    private String content;
}
