package com.safetravel.travel_safe_platform.controller;

import com.safetravel.travel_safe_platform.entity.Region;
import com.safetravel.travel_safe_platform.service.RegionService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 지역 검색·목록 REST API 컨트롤러.
 */
@RestController
public class RegionController {

    private final RegionService regionService;

    public RegionController(RegionService regionService) {
        this.regionService = regionService;
    }

    /**
     * 키워드 또는 시·도·시군구로 지역을 검색합니다.
     */
    @GetMapping("/regions/search")
    public List<Region> searchRegions(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String sido,
            @RequestParam(required = false) String sigungu
    ) {
        if (sido != null && !sido.isBlank()) {
            return regionService.searchRegions(sido, sigungu);
        }

        if (keyword == null || keyword.isBlank()) {
            return List.of();
        }

        return regionService.searchRegions(keyword);
    }

    /**
     * 시·도 목록을 반환합니다.
     */
    @GetMapping("/regions/sido")
    public List<String> getSidoList() {
        return regionService.getSidoList();
    }

    /**
     * 시·도에 속한 시·군·구 목록을 반환합니다.
     */
    @GetMapping("/regions/sigungu")
    public List<Region> getSigunguBySido(
            @RequestParam String sido
    ) {
        return regionService.getSigunguBySido(sido);
    }
}
