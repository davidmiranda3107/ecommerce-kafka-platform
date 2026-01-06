package com.david.ecommerce.notification.service.impl;

import com.david.ecommerce.notification.domain.entity.Notification;
import com.david.ecommerce.notification.domain.enums.NotificationChannel;
import com.david.ecommerce.notification.domain.enums.NotificationStatus;
import com.david.ecommerce.notification.dto.NotificationRequest;
import com.david.ecommerce.notification.dto.NotificationResponse;
import com.david.ecommerce.notification.exception.NotificationNotFoundException;
import com.david.ecommerce.notification.exception.UnsupportedChannelException;
import com.david.ecommerce.notification.repository.NotificationRepository;
import com.david.ecommerce.notification.domain.NotificationSender;
import com.david.ecommerce.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
//@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final Map<NotificationChannel, NotificationSender> senders;

    public NotificationServiceImpl(NotificationRepository notificationRepository, List<NotificationSender> senderList) {
        this.notificationRepository = notificationRepository;
        this.senders = senderList.stream()
                .collect(Collectors.toMap(NotificationSender::getChannel, s -> s));
    }

    @Override
    public NotificationResponse createNotification(NotificationRequest request) {
        Notification notification = Notification.builder()
                .recipient(request.getRecipient())
                .subject(request.getSubject())
                .content(request.getContent())
                .channel(request.getChannel())
                .build();

        return toResponse(notificationRepository.save(notification));
    }

    @Override
    @Async
    public void processNotification(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotificationNotFoundException("Notification not found: " + notificationId));

        try {
            NotificationSender sender = senders.get(notification.getChannel());
            if (sender == null) throw new UnsupportedChannelException("Channel not found: " + notification.getChannel().name());

            sender.send(notification);
            notification.setStatus(NotificationStatus.SENT);
        } catch(Exception ex) {
            log.error("Error sending notification {}: {}", notificationId, ex.getMessage());
            notification.setStatus(NotificationStatus.FAILED);
        }
        notificationRepository.save(notification);
    }

    private NotificationResponse toResponse(Notification notification) {
        return new NotificationResponse(notification.getId(), notification.getStatus());
    }
}
