package com.safetravel.travel_safe_platform.repository;

import com.safetravel.travel_safe_platform.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

// 신고 게시판 DB 작업 담당
public interface ReportRepository
        extends JpaRepository<Report, Long> {

}