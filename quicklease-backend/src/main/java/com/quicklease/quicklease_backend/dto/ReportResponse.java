package com.quicklease.quicklease_backend.dto;

import com.quicklease.quicklease_backend.model.ReportStatus;
import java.time.LocalDateTime;

public class ReportResponse {
    private Long id;
    private String reporterName;
    private String itemTitle;
    private String reason;
    private ReportStatus status;
    private LocalDateTime createdAt;

    public ReportResponse(Long id, String reporterName, String itemTitle, String reason, ReportStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.reporterName = reporterName;
        this.itemTitle = itemTitle;
        this.reason = reason;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getReporterName() { return reporterName; }
    public String getItemTitle() { return itemTitle; }
    public String getReason() { return reason; }
    public ReportStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}