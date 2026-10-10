package com.quicklease.quicklease_backend.dto;

import com.quicklease.quicklease_backend.model.NotificationType;
import java.time.LocalDateTime;

public class NotificationResponse {
    private Long id;
    private NotificationType type;
    private String message;
    private Boolean isRead;
    private LocalDateTime createdAt;

    public NotificationResponse(Long id, NotificationType type, String message, Boolean isRead, LocalDateTime createdAt) {
        this.id = id;
        this.type = type;
        this.message = message;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public NotificationType getType() { return type; }
    public String getMessage() { return message; }
    public Boolean getIsRead() { return isRead; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}