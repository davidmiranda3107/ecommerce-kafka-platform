package com.david.ecommerce.notification.dto;

import com.david.ecommerce.notification.domain.enums.NotificationChannel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationRequest {

    @NotBlank
    private String recipient;

    @NotBlank
    private String subject;

    @NotBlank
    private String content;

    @NotNull
    private NotificationChannel channel;
}
