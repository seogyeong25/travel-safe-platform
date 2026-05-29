package com.safetravel.travel_safe_platform.controller;

import com.safetravel.travel_safe_platform.dto.UserLoginRequest;
import com.safetravel.travel_safe_platform.dto.UserRegisterRequest;
import com.safetravel.travel_safe_platform.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * 회원가입·로그인 화면 MVC 컨트롤러.
 */
@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 회원가입 폼 화면을 표시합니다.
     */
    @GetMapping("/signup")
    public String signupPage(@ModelAttribute("registerRequest") UserRegisterRequest request) {
        return "signup";
    }

    /**
     * 회원가입 요청을 처리합니다.
     */
    @PostMapping("/signup")
    public String signup(
            @Valid @ModelAttribute("registerRequest") UserRegisterRequest request,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "signup";
        }

        try {
            userService.register(request);
        } catch (RuntimeException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "signup";
        }

        return "redirect:/users/login";
    }

    /**
     * 로그인 폼 화면을 표시합니다.
     */
    @GetMapping("/login")
    public String loginPage(@ModelAttribute("loginRequest") UserLoginRequest request) {
        return "login";
    }

}
