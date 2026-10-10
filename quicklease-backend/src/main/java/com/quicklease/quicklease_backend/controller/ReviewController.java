package com.quicklease.quicklease_backend.controller;

import com.quicklease.quicklease_backend.dto.CreateReviewRequest;
import com.quicklease.quicklease_backend.dto.ReviewResponse;
import com.quicklease.quicklease_backend.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public ResponseEntity<?> createReview(@RequestBody CreateReviewRequest request, Authentication authentication) {
        try {
            ReviewResponse response = reviewService.createReview(request, authentication.getName());
            return ResponseEntity.ok(response);
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping("/item/{itemId}")
    public ResponseEntity<List<ReviewResponse>> getReviewsForItem(@PathVariable Long itemId) {
        return ResponseEntity.ok(reviewService.getReviewsForItem(itemId));
    }
}