package com.quicklease.quicklease_backend.dto;

import java.time.LocalDate;

public class CreateRentalRequest {

    private Long itemId;
    private LocalDate startDate;
    private LocalDate endDate;

    public CreateRentalRequest() {
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}