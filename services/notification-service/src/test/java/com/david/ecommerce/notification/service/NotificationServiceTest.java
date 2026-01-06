package com.david.ecommerce.notification.service;

import com.david.ecommerce.notification.domain.entity.Notification;
import com.david.ecommerce.notification.domain.enums.NotificationChannel;
import com.david.ecommerce.notification.domain.enums.NotificationStatus;
import com.david.ecommerce.notification.dto.NotificationRequest;
import com.david.ecommerce.notification.dto.NotificationResponse;
import com.david.ecommerce.notification.repository.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class NotificationServiceTest {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Test
    void shouldCreatedNotificationSuccessfully() {
        NotificationRequest request = new NotificationRequest();
        request.setRecipient("notification-test@ecommerce.com");
        request.setSubject("Notification test");
        request.setContent("This is a notification test");
        request.setChannel(NotificationChannel.EMAIL);

        NotificationResponse response = notificationService.createNotification(request);

        assertNotNull(response.getId());
        assertEquals(NotificationStatus.PENDING, response.getStatus());

    }

    @Test
    void shouldSendNotificationSuccessfully() {
        Notification notification = Notification.builder()
                .recipient("notification-test@ecommerce.com")
                .subject("Notification test")
                .content("This is a notification test")
                .channel(NotificationChannel.EMAIL)
                .build();

        Notification saved = notificationRepository.save(notification);

        notificationService.processNotification(saved.getId());

        Optional<Notification> afterProcess = notificationRepository.findById(saved.getId());

        assertEquals(NotificationStatus.SENT,
                afterProcess.isPresent()
                ? afterProcess.get().getStatus()
                : new Notification().getStatus());
    }

}
