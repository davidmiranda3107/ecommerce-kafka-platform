package com.david.ecommerce.notification.kafka.consumer;

import com.david.ecommerce.notification.domain.enums.NotificationChannel;
import com.david.ecommerce.notification.dto.NotificationRequest;
import com.david.ecommerce.notification.dto.NotificationResponse;
import com.david.ecommerce.notification.kafka.event.PaymentCompletedEvent;
import com.david.ecommerce.notification.kafka.event.PaymentFailedEvent;
import com.david.ecommerce.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentEventConsumer {

    private final NotificationService notificationService;

    @KafkaListener(topics = "payment-completed-topic")
    public void consumePaymentCompleted(PaymentCompletedEvent event) {
        NotificationRequest notificationRequest = NotificationRequest.builder()
                .recipient(event.getRecipient())
                .subject("Payment completed")
                .content("Your order " + event.getOrderId() + " payment has been completed")
                .channel(NotificationChannel.valueOf(event.getChannel()))
                .build();

        NotificationResponse response = notificationService.createNotification(notificationRequest);

        notificationService.processNotification(response.getId());
    }

    @KafkaListener(topics = "payment-failed-topic")
    public void consumePaymentFailed(PaymentFailedEvent event) {
        NotificationRequest notificationRequest = NotificationRequest.builder()
                .recipient(event.getRecipient())
                .subject("Payment completed")
                .content("Your order " + event.getOrderId() + " payment has failed")
                .channel(NotificationChannel.valueOf(event.getChannel()))
                .build();

        NotificationResponse response = notificationService.createNotification(notificationRequest);

        notificationService.processNotification(response.getId());
    }

}
