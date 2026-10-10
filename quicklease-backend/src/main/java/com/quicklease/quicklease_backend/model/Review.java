package com.quicklease.quicklease_backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @ManyToOne
    @JoinColumn(name = "reviewer_id", nullable = false)
    private User reviewer;

    @ManyToOne
    @JoinColumn(name = "rental_id", nullable = false)
    private Rental rental;

    @Column(nullable = false)
    private Integer rating;

    @Column(length = 1000)
    private String comment;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Review() {
    }

    public Review(Item item, User reviewer, Rental rental, Integer rating, String comment) {
        this.item = item;
        this.reviewer = reviewer;
        this.rental = rental;
        this.rating = rating;
        this.comment = comment;
    }

    public Long getId() {
        return id;
    }

    public Item getItem() {
        return item;
    }

    public User getReviewer() {
        return reviewer;
    }

    public Rental getRental() {
        return rental;
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