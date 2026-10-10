package com.quicklease.quicklease_backend.service;

import com.quicklease.quicklease_backend.dto.CreateItemRequest;
import com.quicklease.quicklease_backend.dto.ItemResponse;
import com.quicklease.quicklease_backend.model.Item;
import com.quicklease.quicklease_backend.model.User;
import com.quicklease.quicklease_backend.repository.ItemRepository;
import com.quicklease.quicklease_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.quicklease.quicklease_backend.model.Category;
import org.springframework.data.domain.Sort;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageUploadService imageUploadService;

    public List<ItemResponse> searchItems(String keyword) {
        return itemRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword)
                .stream()
                .map(item -> new ItemResponse(
                        item.getId(),
                        item.getTitle(),
                        item.getDescription(),
                        item.getPricePerDay(),
                        item.getCategory(),
                        item.getImageUrl(),
                        item.getOwner().getFullName()
                ))
                .collect(Collectors.toList());
    }

    public ItemResponse createItem(CreateItemRequest request, String ownerEmail) {

        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new IllegalArgumentException("Owner not found"));
    
        if (owner.getPhone() == null || owner.getCity() == null) {
            throw new IllegalArgumentException("Complete your profile (phone and city) before listing items");
        }
    
        Item item = new Item(
                request.getTitle(),
                request.getDescription(),
                request.getPricePerDay(),
                request.getCategory(),
                request.getSecurityDeposit() != null ? request.getSecurityDeposit() : 0.0,
                owner
        );
    
        Item savedItem = itemRepository.save(item);
    
        return new ItemResponse(
                savedItem.getId(),
                savedItem.getTitle(),
                savedItem.getDescription(),
                savedItem.getPricePerDay(),
                savedItem.getCategory(),
                savedItem.getImageUrl(),
                owner.getFullName()
        );
    }

    public ItemResponse uploadItemImage(Long itemId, MultipartFile file, String requesterEmail) throws IOException {

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));

        if (!item.getOwner().getEmail().equals(requesterEmail)) {
            throw new SecurityException("You are not allowed to modify this item");
        }

        String imageUrl = imageUploadService.uploadImage(file);
        item.setImageUrl(imageUrl);

        Item updatedItem = itemRepository.save(item);

        return new ItemResponse(
                updatedItem.getId(),
                updatedItem.getTitle(),
                updatedItem.getDescription(),
                updatedItem.getPricePerDay(),
                updatedItem.getCategory(),
                updatedItem.getImageUrl(),
                updatedItem.getOwner().getFullName()
        );
    }

    public ItemResponse updateItem(Long itemId, CreateItemRequest request, String requesterEmail) {

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));

        if (!item.getOwner().getEmail().equals(requesterEmail)) {
            throw new SecurityException("You are not allowed to edit this item");
        }

        item.setTitle(request.getTitle());
        item.setDescription(request.getDescription());
        item.setPricePerDay(request.getPricePerDay());
        item.setCategory(request.getCategory());

        Item updatedItem = itemRepository.save(item);

        return new ItemResponse(
                updatedItem.getId(),
                updatedItem.getTitle(),
                updatedItem.getDescription(),
                updatedItem.getPricePerDay(),
                updatedItem.getCategory(),
                updatedItem.getImageUrl(),
                updatedItem.getOwner().getFullName()
        );
    }

    public void deleteItem(Long itemId, String requesterEmail) {

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));

        if (!item.getOwner().getEmail().equals(requesterEmail)) {
            throw new SecurityException("You are not allowed to delete this item");
        }

        itemRepository.delete(item);
    }

    public List<ItemResponse> getAllItems() {
        return itemRepository.findAll()
                .stream()
                .map(item -> new ItemResponse(
                        item.getId(),
                        item.getTitle(),
                        item.getDescription(),
                        item.getPricePerDay(),
                        item.getCategory(),
                        item.getImageUrl(),
                        item.getOwner().getFullName()
                ))
                .collect(Collectors.toList());
    }
    public List<ItemResponse> filterItems(Category category, Double minPrice, Double maxPrice) {

        List<Item> items;
    
        if (category != null && minPrice != null && maxPrice != null) {
            items = itemRepository.findByCategoryAndPricePerDayBetween(category, minPrice, maxPrice);
        } else if (category != null) {
            items = itemRepository.findByCategory(category);
        } else if (minPrice != null && maxPrice != null) {
            items = itemRepository.findByPricePerDayBetween(minPrice, maxPrice);
        } else {
            items = itemRepository.findAll();
        }
    
        return items.stream()
                .map(item -> new ItemResponse(
                        item.getId(),
                        item.getTitle(),
                        item.getDescription(),
                        item.getPricePerDay(),
                        item.getCategory(),
                        item.getImageUrl(),
                        item.getOwner().getFullName()
                ))
                .collect(Collectors.toList());
    }

public List<ItemResponse> getAllItems(String sortBy) {

    Sort sort = getSort(sortBy);

    return itemRepository.findAll(sort)
            .stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
}

private Sort getSort(String sortBy) {
    if (sortBy == null) {
        return Sort.unsorted();
    }
    return switch (sortBy) {
        case "priceAsc" -> Sort.by(Sort.Direction.ASC, "pricePerDay");
        case "priceDesc" -> Sort.by(Sort.Direction.DESC, "pricePerDay");
        case "newest" -> Sort.by(Sort.Direction.DESC, "createdAt");
        default -> Sort.unsorted();
    };
}
public ItemResponse getItemById(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));
    
        return mapToResponse(item);
    }
private ItemResponse mapToResponse(Item item) {
    return new ItemResponse(
            item.getId(),
            item.getTitle(),
            item.getDescription(),
            item.getPricePerDay(),
            item.getCategory(),
            item.getImageUrl(),
            item.getOwner().getFullName()
    );
}

public void addFavorite(Long itemId, String userEmail) {
    User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

    Item item = itemRepository.findById(itemId)
            .orElseThrow(() -> new IllegalArgumentException("Item not found"));

    user.getFavoriteItems().add(item);
    userRepository.save(user);
}

public void removeFavorite(Long itemId, String userEmail) {
    User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

    Item item = itemRepository.findById(itemId)
            .orElseThrow(() -> new IllegalArgumentException("Item not found"));

    user.getFavoriteItems().remove(item);
    userRepository.save(user);
}

public List<ItemResponse> getFavorites(String userEmail) {
    User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

    return user.getFavoriteItems()
            .stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
}
}