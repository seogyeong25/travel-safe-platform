package com.safetravel.travel_safe_platform.service;

import com.safetravel.travel_safe_platform.entity.Region;
import com.safetravel.travel_safe_platform.repository.RegionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegionService {

    // Repository 연결
    private final RegionRepository regionRepository;

    // 생성자 주입
    public RegionService(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }

    // 지역 검색 기능
    public List<Region> searchRegions(String keyword) {

        // 시군구 이름 포함 검색
        return regionRepository.findBySigunguContaining(keyword);
    }
}