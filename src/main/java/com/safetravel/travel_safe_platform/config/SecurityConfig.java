package com.safetravel.travel_safe_platform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // 💡 Spring Security 설정을 활성화하는 어노테이션을 추가합니다.
public class SecurityConfig {

    // 비밀번호 암호화 Bean 등록 (기존 코드 유지)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 💡 URL별 접근 권한 및 로그인/로그아웃 설정을 위한 Bean 추가
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. CSRF 보호 기능 임시 비활성화 (H2 콘솔이나 일반 POST 테스트를 편하게 하기 위함)
                .csrf(csrf -> csrf.disable())

                // 2. URL별 접근 권한 설정
                .authorizeHttpRequests(auth -> auth
                        // 메인페이지, 회원가입, 로그인은 로그인 안 한 사용자도 접근 가능하게 허용
                        .requestMatchers("/", "/users/signup", "/users/login").permitAll()
                        // CSS, JS, 이미지 같은 정적 리소스도 무조건 허용
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                        // 그 외의 모든 요청은 로그인(인증)을 해야만 접근 가능
                        .anyRequest().authenticated()
                )

                // 3. 폼 로그인 설정
                .formLogin(form -> form
                        .loginPage("/users/login")            // 우리가 만든 커스텀 로그인 페이지 URL
                        .loginProcessingUrl("/users/login")   // 로그인 HTML 폼의 action URL과 일치시킴
                        .defaultSuccessUrl("/", true)         // 로그인 성공 시 이동할 메인 페이지
                        .permitAll()
                )

                // 4. 로그아웃 설정
                .logout(logout -> logout
                        .logoutUrl("/users/logout")           // 로그아웃을 처리할 URL
                        .logoutSuccessUrl("/")                // 로그아웃 성공 시 이동할 URL
                        .invalidateHttpSession(true)          // 로그아웃 시 세션 무효화
                        .permitAll()
                );

        return http.build();
    }
}