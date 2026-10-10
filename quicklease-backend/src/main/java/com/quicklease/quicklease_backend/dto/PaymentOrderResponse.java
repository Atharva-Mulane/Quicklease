package com.quicklease.quicklease_backend.dto;

public class PaymentOrderResponse {

    private String razorpayOrderId;
    private String razorpayKeyId;
    private Double amount;
    private Long rentalId;

    public PaymentOrderResponse(String razorpayOrderId, String razorpayKeyId, Double amount, Long rentalId) {
        this.razorpayOrderId = razorpayOrderId;
        this.razorpayKeyId = razorpayKeyId;
        this.amount = amount;
        this.rentalId = rentalId;
    }

    public String getRazorpayOrderId() {
        return razorpayOrderId;
    }

    public String getRazorpayKeyId() {
        return razorpayKeyId;
    }

    public Double getAmount() {
        return amount;
    }

    public Long getRentalId() {
        return rentalId;
    }
}