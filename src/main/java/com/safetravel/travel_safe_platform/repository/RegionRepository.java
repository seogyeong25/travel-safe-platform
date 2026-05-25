package com.safetravel.travel_safe_platform.repository;

import com.safetravel.travel_safe_platform.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegionRepository extends JpaRepository<Region, Long> {

    List<Region> findBysidoContaining(String keyword);  // sido 안에 keyword 포함된 지역 찾기
}