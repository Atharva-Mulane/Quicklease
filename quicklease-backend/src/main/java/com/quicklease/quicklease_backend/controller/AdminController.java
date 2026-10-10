package com.quicklease.quicklease_backend.controller;

import com.quicklease.quicklease_backend.dto.ReportResponse;
import com.quicklease.quicklease_backend.model.ReportStatus;
import com.quicklease.quicklease_backend.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/reports")
    public ResponseEntity<List<ReportResponse>> getAllReports() {
        return ResponseEntity.ok(reportService.getAllReports());
    }

    @PutMapping("/reports/{id}/resolve")
    public ResponseEntity<?> resolveReport(@PathVariable Long id) {
        try {
            ReportResponse response = reportService.resolveReport(id, ReportStatus.RESOLVED);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PutMapping("/reports/{id}/dismiss")
    public ResponseEntity<?> dismissReport(@PathVariable Long id) {
        try {
            ReportResponse response = reportService.resolveReport(id, ReportStatus.DISMISSED);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}