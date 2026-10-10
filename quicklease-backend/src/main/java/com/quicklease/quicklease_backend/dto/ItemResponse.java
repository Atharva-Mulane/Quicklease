package com.quicklease.quicklease_backend.dto;
import com.quicklease.quicklease_backend.model.Category;

public class ItemResponse {

    private Long id;
    private String title;
    private String description;
    private Double pricePerDay;
    private String ownerName;
    private Category category;

    private Double securityDeposit;

    public ItemResponse(Long id, String title, String description, Double pricePerDay, Double securityDeposit,
                        Category category, String imageUrl, String ownerName) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.pricePerDay = pricePerDay;
        this.securityDeposit = securityDeposit;
        this.category = category;
        this.imageUrl = imageUrl;
        this.ownerName = ownerName;
    }
    
    public Double getSecurityDeposit() {
        return securityDeposit;
    }
    private String imageUrl;

// update constructor to accept imageUrl as a parameter, in this order:
public ItemResponse(Long id, String title, String description, Double pricePerDay, Category category, String imageUrl, String ownerName) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.pricePerDay = pricePerDay;
    this.category = category;
    this.imageUrl = imageUrl;
    this.ownerName = ownerName;
}

public String getImageUrl() {
    return imageUrl;
}

    public Category getCategory() {
        return category;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Double getPricePerDay() {
        return pricePerDay;
    }

    public String getOwnerName() {
        return ownerName;
    }
}