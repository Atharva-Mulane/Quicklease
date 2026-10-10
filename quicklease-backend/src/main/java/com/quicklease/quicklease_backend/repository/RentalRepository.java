package com.quicklease.quicklease_backend.repository;

import com.quicklease.quicklease_backend.model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {

    List<Rental> findByItemId(Long itemId);
    List<Rental> findByRenter_Email(String renterEmail);
    List<Rental> findByItem_Owner_Email(String ownerEmail);
}