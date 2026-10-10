package com.quicklease.quicklease_backend.dto;

public class CreateReportRequest {
    private Long itemId;
    private String reason;

    public Long getItemId() { return itemId; }
    public void setItemId(Long itemId) { this.itemId = itemId; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}