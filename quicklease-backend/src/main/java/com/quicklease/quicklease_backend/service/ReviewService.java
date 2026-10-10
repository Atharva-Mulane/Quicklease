package com.quicklease.quicklease_backend.service;

import com.quicklease.quicklease_backend.dto.CreateReviewRequest;
import com.quicklease.quicklease_backend.dto.ReviewResponse;
import com.quicklease.quicklease_backend.model.Rental;
import com.quicklease.quicklease_backend.model.RentalStatus;
import com.quicklease.quicklease_backend.model.Review;
import com.quicklease.quicklease_backend.repository.RentalRepository;
import com.quicklease.quicklease_backend.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private RentalRepository rentalRepository;

    public ReviewResponse createReview(CreateReviewRequest request, String reviewerEmail) {

        Rental rental = rentalRepository.findById(request.getRentalId())
                .orElseThrow(() -> new IllegalArgumentException("Rental not found"));

        if (!rental.getRenter().getEmail().equals(reviewerEmail)) {
            throw new SecurityException("You can only review your own rentals");
        }

        if (rental.getStatus() != RentalStatus.RETURNED) {
            throw new IllegalArgumentException("You can only review completed rentals");
        }

        if (reviewRepository.existsByRental_Id(rental.getId())) {
            throw new IllegalArgumentException("You have already reviewed this rental");
        }

        if (request.getRating() < 1 || request.getRating() > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }

        Review review = new Review(rental.getItem(), rental.getRenter(), rental, request.getRating(), request.getComment());
        Review savedReview = reviewRepository.save(review);

        return mapToResponse(savedReview);
    }

    public List<ReviewResponse> getReviewsForItem(Long itemId) {
        return reviewRepository.findByItem_Id(itemId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private ReviewResponse mapToResponse(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getReviewer().getFullName(),
                review.getRating(),
                review.getComment(),
                review.getCreatedAt()
        );
    }
}