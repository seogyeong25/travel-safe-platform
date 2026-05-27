package com.safetravel.travel_safe_platform.service;

import com.safetravel.travel_safe_platform.entity.User;
import com.safetravel.travel_safe_platform.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. DB에서 해당 username을 가진 유저를 조회합니다.
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다: " + username));

        // 2. Spring Security가 이해할 수 있는 UserDetails 객체로 변환하여 반환합니다.
        // org.springframework.security.core.userdetails.User 객체를 활용합니다.
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword()) // DB에 저장된 암호화된 비밀번호
                .roles("USER")                // 기본 권한 설정 (추후 관리자 기능 등이 필요하면 엔티티에 role 필드 추가)
                .build();
    }
}