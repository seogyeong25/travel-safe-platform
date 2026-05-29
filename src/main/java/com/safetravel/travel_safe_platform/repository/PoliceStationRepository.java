package com.safetravel.travel_safe_platform.repository;

import com.safetravel.travel_safe_platform.entity.PoliceStation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * {@link PoliceStation} 엔티티용 JPA 리포지토리.
 */
public interface PoliceStationRepository
        extends JpaRepository<PoliceStation, Long> {

    /** 지역별 좌표가 있는 경찰 시설 목록 */
    List<PoliceStation> findByRegion_IdAndLatitudeIsNotNullAndLongitudeIsNotNull(
            Long regionId
    );
}