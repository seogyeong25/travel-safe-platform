package com.safetravel.travel_safe_platform.controller;

import com.safetravel.travel_safe_platform.dto.RegionSearchResponseDto;
import com.safetravel.travel_safe_platform.service.RegionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 지역 키워드 검색 REST API 컨트롤러.
 */
@RestController
public class SearchController {

    private final RegionService regionService;

    public SearchController(RegionService regionService) {
        this.regionService = regionService;
    }

    /**
     * 지역 키워드 검색. 항상 200 OK와 JSON(found / multiple / redirectUrl)을 반환합니다.
     * 검색 실패 시에도 404 대신 /region 안내 URL을 담습니다.
     */
    @GetMapping("/api/search")
    public ResponseEntity<RegionSearchResponseDto> searchRegion(
            @RequestParam(value = "keyword", required = false) String keyword
    ) {
        RegionSearchResponseDto response = regionService.searchByKeyword(keyword);
        return ResponseEntity.ok(response);
    }
}
