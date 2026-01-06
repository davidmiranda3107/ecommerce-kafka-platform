package com.david.ecommerce.notification.adapter;

import com.david.ecommerce.notification.domain.NotificationSender;
import com.david.ecommerce.notification.domain.entity.Notification;
import com.david.ecommerce.notification.domain.enums.NotificationChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile("prod")
public class SmsNotificationSender implements NotificationSender {


    @Override
    public NotificationChannel getChannel() { return NotificationChannel.SMS; }

    @Override
    public void send(Notification notification) {
        log.info("Sending SMS to: {}", notification.getRecipient());
    }
}
