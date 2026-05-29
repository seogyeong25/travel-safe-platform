
package com.safetravel.travel_safe_platform.repository;

import com.safetravel.travel_safe_platform.entity.CrimeStat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * {@link CrimeStat} 엔티티용 JPA 리포지토리.
 */
public interface CrimeStatRepository
        extends JpaRepository<CrimeStat, Long> {

    /** 지역 ID로 범죄 통계 목록 조회 */
    List<CrimeStat> findByRegionId(Long regionId);
}

