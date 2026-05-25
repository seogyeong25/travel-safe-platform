package com.safetravel.travel_safe_platform.service;

import com.safetravel.travel_safe_platform.entity.Region;
import com.safetravel.travel_safe_platform.repository.RegionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegionService {

    private final RegionRepository regionRepository;

    public RegionService(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }

    // 지역 검색
    public List<Region> searchRegion(String keyword) {

        return regionRepository.findBysidoContaining(keyword);
    }
}