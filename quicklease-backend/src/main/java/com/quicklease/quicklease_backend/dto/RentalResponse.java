package com.quicklease.quicklease_backend.dto;

import com.quicklease.quicklease_backend.model.RentalStatus;
import java.time.LocalDate;

public class RentalResponse {

    private Long id;
    private String itemTitle;
    private String renterName;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double totalPrice;
    private RentalStatus status;

    public RentalResponse(Long id, String itemTitle, String renterName, LocalDate startDate,
                           LocalDate endDate, Double totalPrice, RentalStatus status) {
        this.id = id;
        this.itemTitle = itemTitle;
        this.renterName = renterName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public String getRenterName() {
        return renterName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public RentalStatus getStatus() {
        return status;
    }
}