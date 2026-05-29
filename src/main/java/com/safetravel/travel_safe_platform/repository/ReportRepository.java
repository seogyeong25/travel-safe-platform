package com.safetravel.travel_safe_platform.repository;

import com.safetravel.travel_safe_platform.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * {@link Report} 엔티티용 JPA 리포지토리.
 */
public interface ReportRepository
        extends JpaRepository<Report, Long> {

    /** 제목 키워드 포함 검색 (페이징) */
    Page<Report> findByTitleContaining(
            String keyword,
            Pageable pageable
    );

    /** 카테고리별 조회 (페이징) */
    Page<Report> findByCategory(
            String category,
            Pageable pageable
    );

    /** 시·도별 조회 (페이징) */
    Page<Report> findByRegion_Sido(String sido, Pageable pageable);

    /** 시·도 + 시군구 포함 검색 (페이징) */
    Page<Report> findByRegion_SidoAndRegion_SigunguContaining(
            String sido,
            String sigungu,
            Pageable pageable
    );

    /** 지역별 최신 글 5건 */
    List<Report> findTop5ByRegion_IdOrderByCreatedAtDesc(Long regionId);

    /** 지역별 전체 글 (최신순) */
    List<Report> findByRegion_IdOrderByCreatedAtDesc(Long regionId);
}