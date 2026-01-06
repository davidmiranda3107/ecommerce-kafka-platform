package com.david.ecommerce.notification.domain;

import com.david.ecommerce.notification.domain.entity.Notification;
import com.david.ecommerce.notification.domain.enums.NotificationChannel;

public interface NotificationSender {

    NotificationChannel getChannel();
    void send(Notification notification);
}
