package com.quicklease.quicklease_backend.controller;

import com.quicklease.quicklease_backend.dto.RegisterRequest;
import com.quicklease.quicklease_backend.dto.LoginRequest;
import com.quicklease.quicklease_backend.dto.UserResponse;
import com.quicklease.quicklease_backend.service.UserService;
import com.quicklease.quicklease_backend.dto.LoginResponse;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            UserResponse response = userService.registerUser(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
   @PostMapping("/login")
public ResponseEntity<?> login(@RequestBody LoginRequest request) {
    try {
        LoginResponse response = userService.loginUser(request);
        return ResponseEntity.ok(response);
    } catch (IllegalArgumentException e) {
        return ResponseEntity.status(401).body(e.getMessage());
    }
}
@GetMapping("/verify-email")
public ResponseEntity<?> verifyEmail(@RequestParam String token) {
    try {
        String result = userService.verifyEmail(token);
        return ResponseEntity.ok(result);
    } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
}