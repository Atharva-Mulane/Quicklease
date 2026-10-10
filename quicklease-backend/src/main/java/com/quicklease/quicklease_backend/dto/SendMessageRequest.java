package com.quicklease.quicklease_backend.dto;

public class SendMessageRequest {
    private String receiverEmail;
    private String content;

    public String getReceiverEmail() { return receiverEmail; }
    public void setReceiverEmail(String receiverEmail) { this.receiverEmail = receiverEmail; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}