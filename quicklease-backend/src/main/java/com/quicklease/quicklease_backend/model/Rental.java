package com.quicklease.quicklease_backend.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "rentals")
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @ManyToOne
    @JoinColumn(name = "renter_id", nullable = false)
    private User renter;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private Double totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RentalStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
protected void onCreate() {
    this.createdAt = LocalDateTime.now();
    this.status = RentalStatus.PENDING;
    this.paymentStatus = PaymentStatus.UNPAID;
}

    public Rental() {
    }

    public Rental(Item item, User renter, LocalDate startDate, LocalDate endDate, Double totalPrice) {
        this.item = item;
        this.renter = renter;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalPrice = totalPrice;
    }
    @Enumerated(EnumType.STRING)
@Column(nullable = false)
private PaymentStatus paymentStatus;

@Column
private String razorpayOrderId;

    public Long getId() {
        return id;
    }

    public Item getItem() {
        return item;
    }

    public User getRenter() {
        return renter;
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

    public void setStatus(RentalStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }
    
    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
    
    public String getRazorpayOrderId() {
        return razorpayOrderId;
    }
    
    public void setRazorpayOrderId(String razorpayOrderId) {
        this.razorpayOrderId = razorpayOrderId;
    }
}