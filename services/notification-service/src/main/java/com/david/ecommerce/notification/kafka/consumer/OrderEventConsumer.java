package com.david.ecommerce.notification.kafka.consumer;

import com.david.ecommerce.notification.domain.entity.Notification;
import com.david.ecommerce.notification.domain.enums.NotificationChannel;
import com.david.ecommerce.notification.dto.NotificationRequest;
import com.david.ecommerce.notification.dto.NotificationResponse;
import com.david.ecommerce.notification.kafka.event.OrderCancelledEvent;
import com.david.ecommerce.notification.kafka.event.OrderCreatedEvent;
import com.david.ecommerce.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderEventConsumer {

    private final NotificationService notificationService;

    @KafkaListener(topics = "order-created-topic")
    public void consumeOrderCreated(OrderCreatedEvent event) {
        NotificationRequest notificationRequest = NotificationRequest.builder()
                .recipient(event.getRecipient())
                .subject("Order created")
                .content("Your order " + event.getOrderId() + " has been created")
                .channel(NotificationChannel.valueOf(event.getChannel()))
                .build();

        NotificationResponse response = notificationService.createNotification(notificationRequest);

        notificationService.processNotification(response.getId());
    }

    @KafkaListener(topics = "order-cancelled-topic")
    public void consumeOrderCancelled(OrderCancelledEvent event) {
        NotificationRequest notificationRequest = NotificationRequest.builder()
                .recipient(event.getRecipient())
                .subject("Order cancelled")
                .content("Your order " + event.getOrderId() + " has been cancelled")
                .channel(NotificationChannel.valueOf(event.getChannel()))
                .build();

        NotificationResponse response = notificationService.createNotification(notificationRequest);

        notificationService.processNotification(response.getId());
    }
}
