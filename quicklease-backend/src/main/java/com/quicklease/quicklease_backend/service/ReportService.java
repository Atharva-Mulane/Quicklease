package com.quicklease.quicklease_backend.service;

import com.quicklease.quicklease_backend.dto.CreateReportRequest;
import com.quicklease.quicklease_backend.dto.ReportResponse;
import com.quicklease.quicklease_backend.model.Item;
import com.quicklease.quicklease_backend.model.Report;
import com.quicklease.quicklease_backend.model.ReportStatus;
import com.quicklease.quicklease_backend.model.User;
import com.quicklease.quicklease_backend.repository.ItemRepository;
import com.quicklease.quicklease_backend.repository.ReportRepository;
import com.quicklease.quicklease_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    public ReportResponse createReport(CreateReportRequest request, String reporterEmail) {

        User reporter = userRepository.findByEmail(reporterEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Item item = itemRepository.findById(request.getItemId())
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));

        Report report = new Report(reporter, item, request.getReason());
        Report saved = reportRepository.save(report);

        return mapToResponse(saved);
    }

    public List<ReportResponse> getAllReports() {
        return reportRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ReportResponse resolveReport(Long reportId, ReportStatus newStatus) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("Report not found"));

        report.setStatus(newStatus);
        Report updated = reportRepository.save(report);

        return mapToResponse(updated);
    }

    private ReportResponse mapToResponse(Report report) {
        return new ReportResponse(
                report.getId(),
                report.getReporter().getFullName(),
                report.getItem().getTitle(),
                report.getReason(),
                report.getStatus(),
                report.getCreatedAt()
        );
    }
}