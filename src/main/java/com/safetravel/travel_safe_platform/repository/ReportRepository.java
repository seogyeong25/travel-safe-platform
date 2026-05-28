package com.safetravel.travel_safe_platform.repository;

import com.safetravel.travel_safe_platform.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReportRepository
        extends JpaRepository<Report, Long> {

    // 제목 검색
    Page<Report> findByTitleContaining(
            String keyword,
            Pageable pageable
    );

    // 카테고리 검색
    Page<Report> findByCategory(
            String category,
            Pageable pageable
    );
}