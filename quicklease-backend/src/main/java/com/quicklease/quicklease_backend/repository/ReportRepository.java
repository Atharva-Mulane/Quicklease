package com.quicklease.quicklease_backend.repository;

import com.quicklease.quicklease_backend.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
}