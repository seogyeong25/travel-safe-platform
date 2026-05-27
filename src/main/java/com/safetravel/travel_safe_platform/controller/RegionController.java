package com.safetravel.travel_safe_platform.controller;

import com.safetravel.travel_safe_platform.entity.Region;
import com.safetravel.travel_safe_platform.service.RegionService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RegionController {

    // Service 연결
    private final RegionService regionService;

    // 생성자 주입
    public RegionController(RegionService regionService) {
        this.regionService = regionService;
    }

    // 지역 검색 API
    // 예시:
    // localhost:8080/regions/search?keyword=강남

    @GetMapping("/regions/search")
    public List<Region> searchRegions(
            @RequestParam String keyword
    ) {

        return regionService.searchRegions(keyword);
    }
}