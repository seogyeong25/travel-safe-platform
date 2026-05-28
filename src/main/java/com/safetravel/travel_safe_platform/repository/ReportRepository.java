package com.safetravel.travel_safe_platform.repository;

import com.safetravel.travel_safe_platform.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository
        extends JpaRepository<Report, Long> {

    // 제목 검색
    List<Report> findByTitleContaining(String keyword);

    List<Report> findByCategory(String category);
}