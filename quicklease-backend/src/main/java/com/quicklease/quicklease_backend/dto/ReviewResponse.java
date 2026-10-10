package com.quicklease.quicklease_backend.dto;

import java.time.LocalDateTime;

public class ReviewResponse {

    private Long id;
    private String reviewerName;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;

    public ReviewResponse(Long id, String reviewerName, Integer rating, String comment, LocalDateTime createdAt) {
        this.id = id;
        this.reviewerName = reviewerName;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public Integer getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}