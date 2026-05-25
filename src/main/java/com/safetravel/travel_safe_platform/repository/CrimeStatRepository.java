package com.safetravel.travel_safe_platform.repository;

import com.safetravel.travel_safe_platform.entity.CrimeStat;
import org.springframework.data.jpa.repository.JpaRepository;

// 범죄 통계 DB 작업 담당
public interface CrimeStatRepository
        extends JpaRepository<CrimeStat, Long> {

}