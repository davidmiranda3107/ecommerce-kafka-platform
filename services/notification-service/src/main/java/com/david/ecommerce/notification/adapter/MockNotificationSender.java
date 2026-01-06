package com.david.ecommerce.notification.adapter;

import com.david.ecommerce.notification.domain.entity.Notification;
import com.david.ecommerce.notification.domain.enums.NotificationChannel;
import com.david.ecommerce.notification.domain.NotificationSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile({"dev", "test"})
@Slf4j
public class MockNotificationSender implements NotificationSender {

    @Override
    public NotificationChannel getChannel() { return NotificationChannel.EMAIL; }

    @Override
    public void send(Notification notification) {
        log.info("Sending EMAIL to {} | {}", notification.getRecipient(), notification.getSubject());
    }
}
