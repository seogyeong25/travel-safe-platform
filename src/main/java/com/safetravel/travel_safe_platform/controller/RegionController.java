package com.safetravel.travel_safe_platform.controller;

import com.safetravel.travel_safe_platform.entity.Region;
import com.safetravel.travel_safe_platform.service.RegionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/regions")
public class RegionController {

    private final RegionService regionService;

    public RegionController(RegionService regionService) {
        this.regionService = regionService;
    }

    // 지역 검색
    @GetMapping("/search")
    public List<Region> searchRegion(
            @RequestParam String keyword) {  // 검색어 받아오기, URL에서 keyword 값 꺼내오기

        return regionService.searchRegion(keyword);
    }
}