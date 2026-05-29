package com.safetravel.travel_safe_platform.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 메인(홈) 화면 MVC 컨트롤러.
 */
@Controller
public class HomeController {

    /**
     * 메인 페이지를 표시합니다.
     *
     * @return index 템플릿명
     */
    @GetMapping("/")
    public String home() {
        return "index";
    }

//    // 로그인 페이지
//    @GetMapping("/login")
//    public String loginPage() {
//        return "login";
//    }
//
//    // 회원가입 페이지
//    @GetMapping("/signup")
//    public String signupPage() {
//        return "signup";
//    }
}
