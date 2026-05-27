package com.safetravel.travel_safe_platform.service;

import com.safetravel.travel_safe_platform.dto.UserRegisterRequest;
import com.safetravel.travel_safe_platform.entity.User;
import com.safetravel.travel_safe_platform.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // =========================
    // 회원가입
    // =========================
    @Transactional
    public User register(UserRegisterRequest request) {

        // 비밀번호 확인 검사
        if (!request.getPassword().equals(request.getPasswordConfirm())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        // 1. username 중복 검사
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("이미 사용중인 아이디입니다.");
        }

        // 2. email 중복 검사
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("이미 사용중인 이메일입니다.");
        }

        // 3. DTO ➔ Entity 변환 및 조립
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .createdAt(LocalDateTime.now())
                .build();

        // 4. DB 저장
        return userRepository.save(user);
    }

    // 💡 기존 직접 구현한 login 메서드는 CustomUserDetailsService가 완벽히 대체하므로 삭제 처리 완료!
}