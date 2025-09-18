package ru.jmdevelop.besshoptgbot.domain.models.user;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Notification {
    private Long id;
    private User user;
    private String title;
    private String message;
    private NotificationType type;
    private boolean isRead;
    private LocalDateTime createdAt;

    public enum NotificationType {
        ORDER_UPDATE, PROMOTION, SYSTEM, SUPPORT
    }
}