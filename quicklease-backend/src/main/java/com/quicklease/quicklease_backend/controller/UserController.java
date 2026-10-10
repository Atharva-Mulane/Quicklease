package com.quicklease.quicklease_backend.controller;

import com.quicklease.quicklease_backend.dto.ProfileRequest;
import com.quicklease.quicklease_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public ResponseEntity<?> getMe(Authentication authentication) {
        try {
            return ResponseEntity.ok(userService.getProfile(authentication.getName()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateMe(@RequestBody ProfileRequest request, Authentication authentication) {
        try {
            return ResponseEntity.ok(userService.updateProfile(authentication.getName(), request));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}