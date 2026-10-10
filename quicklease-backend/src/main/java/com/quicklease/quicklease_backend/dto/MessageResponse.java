package com.quicklease.quicklease_backend.dto;

import java.time.LocalDateTime;

public class MessageResponse {
    private Long id;
    private String senderEmail;
    private String receiverEmail;
    private String content;
    private Boolean isRead;
    private LocalDateTime sentAt;

    public MessageResponse(Long id, String senderEmail, String receiverEmail, String content, Boolean isRead, LocalDateTime sentAt) {
        this.id = id;
        this.senderEmail = senderEmail;
        this.receiverEmail = receiverEmail;
        this.content = content;
        this.isRead = isRead;
        this.sentAt = sentAt;
    }

    public Long getId() { return id; }
    public String getSenderEmail() { return senderEmail; }
    public String getReceiverEmail() { return receiverEmail; }
    public String getContent() { return content; }
    public Boolean getIsRead() { return isRead; }
    public LocalDateTime getSentAt() { return sentAt; }
}