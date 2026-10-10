package com.quicklease.quicklease_backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/api/health")
    public String checkHealth() {
        return "QuickLease backend is running!";
    }

    @GetMapping("/api/protected-test")
public String protectedTest() {
    return "You are authenticated! This endpoint requires a valid JWT.";
}
@GetMapping("/api/admin/test")
public String adminTest() {
    return "You are an ADMIN. This is a protected admin route.";
}
}

//eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJyZWd1bGFydXNlckBxdWlja2xlYXNlLmNvbSIsInJvbGUiOiJVU0VSIiwiaWF0IjoxNzg3MTI2NDY2LCJleHAiOjE3ODcyMTI4NjZ9.x89Sx9wOZ1mUhKLw87Wlxr5yD20h1Ibhd4F_48PbfJObt0i2WHJTIYMpH3knprrN