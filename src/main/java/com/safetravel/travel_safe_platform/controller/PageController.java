package com.safetravel.travel_safe_platform.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    // 지역 검색 페이지
    @GetMapping("/region")
    public String regionPage() {
        return "region";
    }

    // 안전 리포트 페이지
    @GetMapping("/report")
    public String reportPage() {
        return "report";
    }

    // 지도 페이지
    @GetMapping("/map")
    public String mapPage() {
        return "map";
    }

    // 예방 가이드 페이지
    @GetMapping("/guide")
    public String guidePage() {
        return "guide";
    }

}