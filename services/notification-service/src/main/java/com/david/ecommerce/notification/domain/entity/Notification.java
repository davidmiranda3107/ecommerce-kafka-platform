package com.david.ecommerce.notification.domain.entity;

import com.david.ecommerce.notification.domain.enums.NotificationChannel;
import com.david.ecommerce.notification.domain.enums.NotificationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String recipient;

    private String subject;

    @Column(length = 2000)
    private String content;

    @Enumerated(EnumType.STRING)
    private NotificationChannel channel;

    @Enumerated(EnumType.STRING)
    private NotificationStatus status;

    private LocalDateTime createdAt;

    @PrePersist
    void prePersist() {
        this.status = NotificationStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }
}
