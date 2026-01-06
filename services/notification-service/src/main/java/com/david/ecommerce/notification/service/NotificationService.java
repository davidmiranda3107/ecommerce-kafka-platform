package com.david.ecommerce.notification.service;

import com.david.ecommerce.notification.dto.NotificationRequest;
import com.david.ecommerce.notification.dto.NotificationResponse;

public interface NotificationService {

    NotificationResponse createNotification(NotificationRequest request);
    void processNotification(Long notificationId);
}
