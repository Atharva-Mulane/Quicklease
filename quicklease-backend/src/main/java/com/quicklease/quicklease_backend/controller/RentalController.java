package com.quicklease.quicklease_backend.controller;

import com.quicklease.quicklease_backend.dto.CreateRentalRequest;
import com.quicklease.quicklease_backend.dto.PaymentOrderResponse;
import com.quicklease.quicklease_backend.dto.RentalResponse;
import com.quicklease.quicklease_backend.dto.VerifyPaymentRequest;
import com.quicklease.quicklease_backend.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    @Autowired
    private RentalService rentalService;

    @PostMapping
    public ResponseEntity<?> createRental(@RequestBody CreateRentalRequest request, Authentication authentication) {
        try {
            RentalResponse response = rentalService.createRental(request, authentication.getName());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/accept")
public ResponseEntity<?> acceptRental(@PathVariable Long id, Authentication authentication) {
    try {
        RentalResponse response = rentalService.acceptRental(id, authentication.getName());
        return ResponseEntity.ok(response);
    } catch (SecurityException e) {
        return ResponseEntity.status(403).body(e.getMessage());
    } catch (IllegalArgumentException e) {
        return ResponseEntity.status(400).body(e.getMessage());
    }
}

@PutMapping("/{id}/reject")
public ResponseEntity<?> rejectRental(@PathVariable Long id, Authentication authentication) {
    try {
        RentalResponse response = rentalService.rejectRental(id, authentication.getName());
        return ResponseEntity.ok(response);
    } catch (SecurityException e) {
        return ResponseEntity.status(403).body(e.getMessage());
    } catch (IllegalArgumentException e) {
        return ResponseEntity.status(400).body(e.getMessage());
    }
}

@PostMapping("/{id}/pay")
public ResponseEntity<?> initiatePayment(@PathVariable Long id, Authentication authentication) {
    try {
        PaymentOrderResponse response = rentalService.initiatePayment(id, authentication.getName());
        return ResponseEntity.ok(response);
    } catch (SecurityException e) {
        return ResponseEntity.status(403).body(e.getMessage());
    } catch (Exception e) {
        return ResponseEntity.status(400).body(e.getMessage());
    }
}

@PostMapping("/verify-payment")
public ResponseEntity<?> verifyPayment(@RequestBody VerifyPaymentRequest request, Authentication authentication) {
    try {
        RentalResponse response = rentalService.verifyPayment(request, authentication.getName());
        return ResponseEntity.ok(response);
    } catch (SecurityException e) {
        return ResponseEntity.status(403).body(e.getMessage());
    } catch (Exception e) {
        return ResponseEntity.status(400).body(e.getMessage());
    }
}
@PutMapping("/{id}/activate")
public ResponseEntity<?> markAsActive(@PathVariable Long id, Authentication authentication) {
    try {
        RentalResponse response = rentalService.markAsActive(id, authentication.getName());
        return ResponseEntity.ok(response);
    } catch (SecurityException e) {
        return ResponseEntity.status(403).body(e.getMessage());
    } catch (IllegalArgumentException e) {
        return ResponseEntity.status(400).body(e.getMessage());
    }
}
@PutMapping("/{id}/return")
public ResponseEntity<?> markAsReturned(@PathVariable Long id, Authentication authentication) {
    try {
        RentalResponse response = rentalService.markAsReturned(id, authentication.getName());
        return ResponseEntity.ok(response);
    } catch (SecurityException e) {
        return ResponseEntity.status(403).body(e.getMessage());
    } catch (IllegalArgumentException e) {
        return ResponseEntity.status(400).body(e.getMessage());
    }
}
@GetMapping("/my-rentals")
public ResponseEntity<List<RentalResponse>> getMyRentals(Authentication authentication) {
    return ResponseEntity.ok(rentalService.getMyRentals(authentication.getName()));
}

@GetMapping("/owner-requests")
public ResponseEntity<List<RentalResponse>> getRentalsOnMyItems(Authentication authentication) {
    return ResponseEntity.ok(rentalService.getRentalsOnMyItems(authentication.getName()));
}
}