package com.quicklease.quicklease_backend.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "items")
public class Item {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private Double pricePerDay;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(length = 500)
private String imageUrl;

@Column(nullable = false, updatable = false)
private LocalDateTime createdAt;

@Column(nullable = false)
private Double securityDeposit = 0.0;

public Double getSecurityDeposit() {
    return securityDeposit;
}

public void setSecurityDeposit(Double securityDeposit) {
    this.securityDeposit = securityDeposit;
}

@PrePersist
protected void onCreate() {
    this.createdAt = LocalDateTime.now();
}

public LocalDateTime getCreatedAt() {
    return createdAt;
}


public String getImageUrl() {
    return imageUrl;
}

public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
}

    public Item() {
    }
    public Item(String title, String description, Double pricePerDay, Category category, Double securityDeposit, User owner) {
        this.title = title;
        this.description = description;
        this.pricePerDay = pricePerDay;
        this.category = category;
        this.securityDeposit = securityDeposit;
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(Double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Category getCategory() {
        return category;
    }
    
    public void setCategory(Category category) {
        this.category = category;
    }
}