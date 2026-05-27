package com.safetravel.travel_safe_platform.controller;

import com.safetravel.travel_safe_platform.dto.UserLoginRequest;
import com.safetravel.travel_safe_platform.dto.UserRegisterRequest;
import com.safetravel.travel_safe_platform.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 회원가입 화면
    @GetMapping("/signup")
    public String signupPage(@ModelAttribute("registerRequest") UserRegisterRequest request) {
        return "signup";
    }

    // 회원가입 처리
    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute("registerRequest") UserRegisterRequest request,
                         BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "signup"; // 에러가 있으면 담아서 다시 회원가입 페이지로
        }

        userService.register(request); // 서비스단에 DTO 전달
        return "redirect:/users/login"; // 가입 성공 시 로그인 페이지로 이동
    }

    // 로그인 화면
    @GetMapping("/login")
    public String loginPage(@ModelAttribute("loginRequest") UserLoginRequest request) {
        return "login";
    }

    // 로그인 처리 (Spring Security를 쓰지 않고 직접 구현할 경우 예시)
    @PostMapping("/login")
    public String login(@ModelAttribute("loginRequest") UserLoginRequest request) {
        // userService.login(request); 등의 로직 수행
        return "redirect:/";
    }
}