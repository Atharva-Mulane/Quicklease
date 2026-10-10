package com.quicklease.quicklease_backend.controller;

import com.quicklease.quicklease_backend.dto.CreateItemRequest;
import com.quicklease.quicklease_backend.dto.ItemResponse;
import com.quicklease.quicklease_backend.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.quicklease.quicklease_backend.model.Category;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    @GetMapping
    public ResponseEntity<List<ItemResponse>> getAllItems(@RequestParam(required = false) String sortBy) {
        return ResponseEntity.ok(itemService.getAllItems(sortBy));
    }

    @Autowired
    private ItemService itemService;

    @PostMapping
    public ResponseEntity<?> createItem(@RequestBody CreateItemRequest request, Authentication authentication) {
        try {
            String ownerEmail = authentication.getName();
            ItemResponse response = itemService.createItem(request, ownerEmail);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
public ResponseEntity<?> updateItem(@PathVariable Long id, @RequestBody CreateItemRequest request, Authentication authentication) {
    try {
        String requesterEmail = authentication.getName();
        ItemResponse response = itemService.updateItem(id, request, requesterEmail);
        return ResponseEntity.ok(response);
    } catch (SecurityException e) {
        return ResponseEntity.status(403).body(e.getMessage());
    } catch (IllegalArgumentException e) {
        return ResponseEntity.status(404).body(e.getMessage());
    }
}

@PostMapping("/{id}/image")
public ResponseEntity<?> uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file, Authentication authentication) {
    try {
        String requesterEmail = authentication.getName();
        ItemResponse response = itemService.uploadItemImage(id, file, requesterEmail);
        return ResponseEntity.ok(response);
    } catch (SecurityException e) {
        return ResponseEntity.status(403).body(e.getMessage());
    } catch (IllegalArgumentException e) {
        return ResponseEntity.status(404).body(e.getMessage());
    } catch (Exception e) {
        return ResponseEntity.status(500).body("Upload failed: " + e.getMessage());
    }
}
@GetMapping("/search")
public ResponseEntity<List<ItemResponse>> searchItems(@RequestParam String keyword) {
    return ResponseEntity.ok(itemService.searchItems(keyword));
}
@GetMapping("/filter")
public ResponseEntity<List<ItemResponse>> filterItems(
        @RequestParam(required = false) Category category,
        @RequestParam(required = false) Double minPrice,
        @RequestParam(required = false) Double maxPrice) {
    return ResponseEntity.ok(itemService.filterItems(category, minPrice, maxPrice));
}
@GetMapping("/{id}")
public ResponseEntity<?> getItemById(@PathVariable Long id) {
    try {
        ItemResponse response = itemService.getItemById(id);
        return ResponseEntity.ok(response);
    } catch (IllegalArgumentException e) {
        return ResponseEntity.status(404).body(e.getMessage());
    }
}
@PostMapping("/{id}/favorite")
public ResponseEntity<?> addFavorite(@PathVariable Long id, Authentication authentication) {
    try {
        itemService.addFavorite(id, authentication.getName());
        return ResponseEntity.ok("Added to favorites");
    } catch (IllegalArgumentException e) {
        return ResponseEntity.status(404).body(e.getMessage());
    }
}

@DeleteMapping("/{id}/favorite")
public ResponseEntity<?> removeFavorite(@PathVariable Long id, Authentication authentication) {
    try {
        itemService.removeFavorite(id, authentication.getName());
        return ResponseEntity.ok("Removed from favorites");
    } catch (IllegalArgumentException e) {
        return ResponseEntity.status(404).body(e.getMessage());
    }
}

@GetMapping("/favorites")
public ResponseEntity<?> getFavorites(Authentication authentication) {
    return ResponseEntity.ok(itemService.getFavorites(authentication.getName()));
}
@DeleteMapping("/{id}")
public ResponseEntity<?> deleteItem(@PathVariable Long id, Authentication authentication) {
    try {
        String requesterEmail = authentication.getName();
        itemService.deleteItem(id, requesterEmail);
        return ResponseEntity.ok("Item deleted successfully");
    } catch (SecurityException e) {
        return ResponseEntity.status(403).body(e.getMessage());
    } catch (IllegalArgumentException e) {
        return ResponseEntity.status(404).body(e.getMessage());
    }
}
}