package com.safetravel.travel_safe_platform.repository;

import com.safetravel.travel_safe_platform.entity.PoliceStation;
import org.springframework.data.jpa.repository.JpaRepository;

// 경찰서 위치 DB 작업 담당
public interface PoliceStationRepository
        extends JpaRepository<PoliceStation, Long> {

}