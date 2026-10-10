package com.quicklease.quicklease_backend.service;

import com.quicklease.quicklease_backend.dto.NotificationResponse;
import com.quicklease.quicklease_backend.model.Notification;
import com.quicklease.quicklease_backend.model.NotificationType;
import com.quicklease.quicklease_backend.model.User;
import com.quicklease.quicklease_backend.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public void createNotification(User user, NotificationType type, String message) {
        Notification notification = new Notification(user, type, message);
        notificationRepository.save(notification);
    }

    public List<NotificationResponse> getMyNotifications(String email) {
        return notificationRepository.findByUser_EmailOrderByCreatedAtDesc(email)
                .stream()
                .map(n -> new NotificationResponse(n.getId(), n.getType(), n.getMessage(), n.getIsRead(), n.getCreatedAt()))
                .collect(Collectors.toList());
    }

    public void markAsRead(Long id, String requesterEmail) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));

        if (!notification.getUser().getEmail().equals(requesterEmail)) {
            throw new SecurityException("Not your notification");
        }

        notification.setIsRead(true);
        notificationRepository.save(notification);
    }
}