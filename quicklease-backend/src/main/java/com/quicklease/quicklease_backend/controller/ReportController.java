package com.quicklease.quicklease_backend.controller;

import com.quicklease.quicklease_backend.dto.CreateReportRequest;
import com.quicklease.quicklease_backend.dto.ReportResponse;
import com.quicklease.quicklease_backend.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping
    public ResponseEntity<?> createReport(@RequestBody CreateReportRequest request, Authentication authentication) {
        try {
            ReportResponse response = reportService.createReport(request, authentication.getName());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}