package com.david.ecommerce.notification.dto;

import com.david.ecommerce.notification.domain.enums.NotificationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotificationResponse {
    private Long id;
    private NotificationStatus status;
}
