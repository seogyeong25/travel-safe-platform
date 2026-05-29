package com.safetravel.travel_safe_platform.repository;

import com.safetravel.travel_safe_platform.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * {@link Region} 엔티티용 JPA 리포지토리.
 */
public interface RegionRepository
        extends JpaRepository<Region, Long> {

    /** 시군구명 부분 일치 검색 */
    List<Region> findBySigunguContaining(String keyword);

    /** 시군구명 정확 일치 */
    Optional<Region> findBySigungu(String sigungu);

    /** 시·도 + 시군구 정확 일치 */
    Optional<Region> findBySidoAndSigungu(
            String sido,
            String sigungu
    );

    /** 중복 제거된 시·도 목록 */
    @Query("SELECT DISTINCT r.sido FROM Region r ORDER BY r.sido")
    List<String> findDistinctSido();

    /** 시·도별 시군구 목록 (가나다순) */
    List<Region> findBySidoOrderBySigunguAsc(String sido);

    /** 시·도 + 시군구 부분 일치 */
    List<Region> findBySidoAndSigunguContainingOrderBySigunguAsc(
            String sido,
            String sigungu
    );

    /** 시·도명 부분 일치 */
    List<Region> findBySidoContainingOrderBySigunguAsc(String sido);

    /** 시·도 또는 시군구 부분 일치 */
    List<Region> findBySidoContainingOrSigunguContainingOrderBySigunguAsc(
            String sido,
            String sigungu
    );
}

