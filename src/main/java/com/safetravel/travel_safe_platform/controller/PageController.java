package com.safetravel.travel_safe_platform.controller;

import com.safetravel.travel_safe_platform.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 지역 선택·안전 리포트·지도·가이드 등 정적 페이지 MVC 컨트롤러.
 */
@Controller
public class PageController {

    private final RegionRepository regionRepository;

    @Value("${kakao.map.javascript-key}")
    private String kakaoMapJavascriptKey;

    public PageController(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }

    /** 지역 선택 페이지 */
    @GetMapping("/region")
    public String regionPage(Model model) {
        model.addAttribute("regions", regionRepository.findAll());
        model.addAttribute("sidoList", regionRepository.findDistinctSido());
        return "region";
    }

    /** 안전 리포트 페이지 */
    @GetMapping("/report")
    public String reportPage(Model model) {
        model.addAttribute("kakaoMapKey", kakaoMapJavascriptKey);
        return "report";
    }

    /** 지도 페이지 */
    @GetMapping("/map")
    public String mapPage(Model model) {
        model.addAttribute("kakaoMapKey", kakaoMapJavascriptKey);
        model.addAttribute("regions", regionRepository.findAll());
        model.addAttribute("sidoList", regionRepository.findDistinctSido());
        return "map";
    }

    /** 예방 가이드 페이지 */
    @GetMapping("/guide")
    public String guidePage() {
        return "guide";
    }

}
