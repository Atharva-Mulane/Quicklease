package com.quicklease.quicklease_backend.repository;

import com.quicklease.quicklease_backend.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByItem_Id(Long itemId);

    boolean existsByRental_Id(Long rentalId);
}