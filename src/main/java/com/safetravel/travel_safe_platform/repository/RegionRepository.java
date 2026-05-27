package com.safetravel.travel_safe_platform.repository;

import com.safetravel.travel_safe_platform.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegionRepository extends JpaRepository<Region, Long> {

    // sigungu(시군구) 포함 검색
    List<Region> findBySigunguContaining(String keyword);
}