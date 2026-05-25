package com.safetravel.travel_safe_platform.controller;

import com.safetravel.travel_safe_platform.entity.User;
import com.safetravel.travel_safe_platform.service.UserService;
import com.safetravel.travel_safe_platform.dto.UserLoginRequest;
import com.safetravel.travel_safe_platform.dto.UserRegisterRequest;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 회원가입
    @PostMapping("/register")
    public User register(
            @Valid
            @RequestBody UserRegisterRequest request) {

        User user = new User();

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        return userService.register(user);
    }

    // 로그인
    @PostMapping("/login")
    public User login(@RequestBody UserLoginRequest request) {

        return userService.login(
                request.getUsername(),
                request.getPassword()
        );
    }
}