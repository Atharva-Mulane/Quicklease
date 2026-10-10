package com.quicklease.quicklease_backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reporter_id", nullable = false)
    private User reporter;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(nullable = false, length = 1000)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.status = ReportStatus.PENDING;
    }

    public Report() {
    }

    public Report(User reporter, Item item, String reason) {
        this.reporter = reporter;
        this.item = item;
        this.reason = reason;
    }

    public Long getId() { return id; }
    public User getReporter() { return reporter; }
    public Item getItem() { return item; }
    public String getReason() { return reason; }
    public ReportStatus getStatus() { return status; }
    public void setStatus(ReportStatus status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}