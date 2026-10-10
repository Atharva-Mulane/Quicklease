package com.quicklease.quicklease_backend.repository;

import com.quicklease.quicklease_backend.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.quicklease.quicklease_backend.model.Category;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String titleKeyword, String descriptionKeyword);
    List<Item> findByCategoryAndPricePerDayBetween(Category category, Double minPrice, Double maxPrice);

    List<Item> findByCategory(Category category);

    List<Item> findByPricePerDayBetween(Double minPrice, Double maxPrice);

    List<Item> findAll(org.springframework.data.domain.Sort sort);
}