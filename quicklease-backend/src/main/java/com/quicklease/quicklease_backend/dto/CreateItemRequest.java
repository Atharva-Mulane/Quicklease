package com.quicklease.quicklease_backend.dto;
import com.quicklease.quicklease_backend.model.Category;

public class CreateItemRequest {

    private String title;
    private String description;
    private Double pricePerDay;
    private Category category;

    private Double securityDeposit;

    public Double getSecurityDeposit() {
        return securityDeposit;
    }

    public void setSecurityDeposit(Double securityDeposit) {
        this.securityDeposit = securityDeposit;
    }
    public CreateItemRequest() {
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


public Category getCategory() {
    return category;
}

public void setCategory(Category category) {
    this.category = category;
}
}