package com.safetravel.travel_safe_platform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    @Bean // Spring이 관리할 객체 등록
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }
}