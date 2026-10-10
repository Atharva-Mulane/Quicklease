package com.quicklease.quicklease_backend.service;

import com.quicklease.quicklease_backend.dto.CreateRentalRequest;
import com.quicklease.quicklease_backend.dto.PaymentOrderResponse;
import com.quicklease.quicklease_backend.dto.RentalResponse;
import com.quicklease.quicklease_backend.model.Rental;
import com.quicklease.quicklease_backend.model.RentalStatus;
import com.quicklease.quicklease_backend.repository.ItemRepository;
import com.quicklease.quicklease_backend.repository.RentalRepository;
import com.quicklease.quicklease_backend.repository.UserRepository;
import com.quicklease.quicklease_backend.model.NotificationType;
import com.razorpay.RazorpayClient;
import com.quicklease.quicklease_backend.model.Item;
import com.quicklease.quicklease_backend.model.PaymentStatus;
import com.quicklease.quicklease_backend.model.User;
import java.util.stream.Collectors;


import com.razorpay.Order;
import com.razorpay.Utils;
import org.json.JSONObject;
import com.quicklease.quicklease_backend.dto.VerifyPaymentRequest;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class RentalService {

    @Autowired
    private RentalRepository rentalRepository;

    public boolean isAvailable(Long itemId, LocalDate startDate, LocalDate endDate) {

        List<Rental> existingRentals = rentalRepository.findByItemId(itemId);

        for (Rental rental : existingRentals) {

            if (rental.getStatus() == RentalStatus.REJECTED) {
                continue;
            }

            boolean overlaps = !startDate.isAfter(rental.getEndDate())
                    && !endDate.isBefore(rental.getStartDate());

            if (overlaps) {
                return false;
            }
        }

        return true;
    }

    public Double calculateTotalPrice(LocalDate startDate, LocalDate endDate, Double pricePerDay) {
        long numberOfDays = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        return numberOfDays * pricePerDay;
    }

    @Autowired
private ItemRepository itemRepository;

@Autowired
private UserRepository userRepository;

@Autowired
private NotificationService notificationService;

public RentalResponse createRental(CreateRentalRequest request, String renterEmail) {

    Item item = itemRepository.findById(request.getItemId())
            .orElseThrow(() -> new IllegalArgumentException("Item not found"));

    User renter = userRepository.findByEmail(renterEmail)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

    if (renter.getPhone() == null || renter.getCity() == null) {
        throw new IllegalArgumentException("Complete your profile (phone and city) before renting");
    }

    if (item.getOwner().getEmail().equals(renterEmail)) {
        throw new IllegalArgumentException("You cannot rent your own item");
    }

    if (request.getStartDate().isBefore(LocalDate.now(ZoneId.of("Asia/Kolkata")))) {
        throw new IllegalArgumentException("Start date cannot be in the past");
    }

    if (request.getEndDate().isBefore(request.getStartDate())) {
        throw new IllegalArgumentException("End date cannot be before start date");
    }

    if (!isAvailable(request.getItemId(), request.getStartDate(), request.getEndDate())) {
        throw new IllegalArgumentException("Item is not available for the selected dates");
    }

    Double rentalCost = calculateTotalPrice(request.getStartDate(), request.getEndDate(), item.getPricePerDay());
    Double totalPrice = rentalCost + item.getSecurityDeposit();

    Rental rental = new Rental(item, renter, request.getStartDate(), request.getEndDate(), totalPrice);

    Rental savedRental = rentalRepository.save(rental);

    notificationService.createNotification(item.getOwner(), NotificationType.RENTAL_REQUEST,
            renter.getFullName() + " requested to rent your item: " + item.getTitle());

    return mapToResponse(savedRental);
}

private RentalResponse mapToResponse(Rental rental) {
    return new RentalResponse(
            rental.getId(),
            rental.getItem().getTitle(),
            rental.getRenter().getFullName(),
            rental.getStartDate(),
            rental.getEndDate(),
            rental.getTotalPrice(),
            rental.getStatus()
    );
}
public RentalResponse acceptRental(Long rentalId, String requesterEmail) {

    Rental rental = rentalRepository.findById(rentalId)
            .orElseThrow(() -> new IllegalArgumentException("Rental not found"));

    if (!rental.getItem().getOwner().getEmail().equals(requesterEmail)) {
        throw new SecurityException("You are not authorized to respond to this rental");
    }

    if (rental.getStatus() != RentalStatus.PENDING) {
        throw new IllegalArgumentException("Only pending rentals can be accepted");
    }

    rental.setStatus(RentalStatus.ACCEPTED);
    Rental updatedRental = rentalRepository.save(rental);

    notificationService.createNotification(rental.getRenter(), NotificationType.RENTAL_ACCEPTED,
        "Your rental request for " + rental.getItem().getTitle() + " was accepted!");

    return mapToResponse(updatedRental);
}

public RentalResponse rejectRental(Long rentalId, String requesterEmail) {

    Rental rental = rentalRepository.findById(rentalId)
            .orElseThrow(() -> new IllegalArgumentException("Rental not found"));

    if (!rental.getItem().getOwner().getEmail().equals(requesterEmail)) {
        throw new SecurityException("You are not authorized to respond to this rental");
    }

    if (rental.getStatus() != RentalStatus.PENDING) {
        throw new IllegalArgumentException("Only pending rentals can be rejected");
    }

    rental.setStatus(RentalStatus.REJECTED);
    Rental updatedRental = rentalRepository.save(rental);

    notificationService.createNotification(rental.getRenter(), NotificationType.RENTAL_REJECTED,
        "Your rental request for " + rental.getItem().getTitle() + " was rejected.");

    return mapToResponse(updatedRental);
}
@Value("${razorpay.key-id}")
private String razorpayKeyId;

@Autowired
private RazorpayClient razorpayClient;

public PaymentOrderResponse initiatePayment(Long rentalId, String requesterEmail) throws Exception {

    Rental rental = rentalRepository.findById(rentalId)
            .orElseThrow(() -> new IllegalArgumentException("Rental not found"));

    if (!rental.getRenter().getEmail().equals(requesterEmail)) {
        throw new SecurityException("You are not authorized to pay for this rental");
    }

    if (rental.getStatus() != RentalStatus.ACCEPTED) {
        throw new IllegalArgumentException("Only accepted rentals can be paid for");
    }

    if (rental.getPaymentStatus() == PaymentStatus.PAID) {
        throw new IllegalArgumentException("This rental has already been paid for");
    }

    int amountInPaise = (int) (rental.getTotalPrice() * 100);

    JSONObject orderRequest = new JSONObject();
    orderRequest.put("amount", amountInPaise);
    orderRequest.put("currency", "INR");
    orderRequest.put("receipt", "rental_" + rental.getId());

    Order order = razorpayClient.orders.create(orderRequest);

    rental.setRazorpayOrderId(order.get("id"));
    rentalRepository.save(rental);

    return new PaymentOrderResponse(order.get("id"), razorpayKeyId, rental.getTotalPrice(), rental.getId());
}
@Value("${razorpay.key-secret}")
private String razorpayKeySecret;
public RentalResponse verifyPayment(VerifyPaymentRequest request, String requesterEmail) throws Exception {

    Rental rental = rentalRepository.findAll().stream()
            .filter(r -> request.getRazorpayOrderId().equals(r.getRazorpayOrderId()))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Rental not found for this order"));

    if (!rental.getRenter().getEmail().equals(requesterEmail)) {
        throw new SecurityException("You are not authorized to verify this payment");
    }

    JSONObject options = new JSONObject();
    options.put("razorpay_order_id", request.getRazorpayOrderId());
    options.put("razorpay_payment_id", request.getRazorpayPaymentId());
    options.put("razorpay_signature", request.getRazorpaySignature());

    boolean isValidSignature = Utils.verifyPaymentSignature(options, razorpayKeySecret);

    if (!isValidSignature) {
        rental.setPaymentStatus(PaymentStatus.FAILED);
        rentalRepository.save(rental);
        throw new IllegalArgumentException("Payment verification failed");
    }

    rental.setPaymentStatus(PaymentStatus.PAID);
    Rental updatedRental = rentalRepository.save(rental);

    return mapToResponse(updatedRental);
}
public RentalResponse markAsActive(Long rentalId, String requesterEmail) {

    Rental rental = rentalRepository.findById(rentalId)
            .orElseThrow(() -> new IllegalArgumentException("Rental not found"));

    if (!rental.getItem().getOwner().getEmail().equals(requesterEmail)) {
        throw new SecurityException("You are not authorized to update this rental");
    }

    if (rental.getStatus() != RentalStatus.ACCEPTED) {
        throw new IllegalArgumentException("Only accepted rentals can be marked as active");
    }

    if (rental.getPaymentStatus() != PaymentStatus.PAID) {
        throw new IllegalArgumentException("Payment must be completed before marking as active");
    }

    rental.setStatus(RentalStatus.ACTIVE);
    Rental updatedRental = rentalRepository.save(rental);

    return mapToResponse(updatedRental);
}
public RentalResponse markAsReturned(Long rentalId, String requesterEmail) {

    Rental rental = rentalRepository.findById(rentalId)
            .orElseThrow(() -> new IllegalArgumentException("Rental not found"));

    if (!rental.getItem().getOwner().getEmail().equals(requesterEmail)) {
        throw new SecurityException("You are not authorized to update this rental");
    }

    if (rental.getStatus() != RentalStatus.ACTIVE) {
        throw new IllegalArgumentException("Only active rentals can be marked as returned");
    }

    rental.setStatus(RentalStatus.RETURNED);
    Rental updatedRental = rentalRepository.save(rental);

    return mapToResponse(updatedRental);
}
public List<RentalResponse> getMyRentals(String renterEmail) {
    return rentalRepository.findByRenter_Email(renterEmail)
            .stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
}

public List<RentalResponse> getRentalsOnMyItems(String ownerEmail) {
    return rentalRepository.findByItem_Owner_Email(ownerEmail)
            .stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
}
}