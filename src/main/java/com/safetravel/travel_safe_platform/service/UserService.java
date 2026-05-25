package com.safetravel.travel_safe_platform.service;

import com.safetravel.travel_safe_platform.entity.User;
import com.safetravel.travel_safe_platform.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    // 회원가입
    public User register(User user) {

        // username 중복 검사
        if (userRepository.existsByUsername(user.getUsername())) {

            throw new RuntimeException("이미 사용중인 아이디");
        }

        // email 중복 검사
        if (userRepository.existsByEmail(user.getEmail())) {

            throw new RuntimeException("이미 사용중인 이메일");
        }

        user.setCreatedAt(LocalDateTime.now());

        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        return userRepository.save(user);
    }


    // 로그인
    public User login(String username, String password) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("비밀번호 틀림");
        }

        return user;
    }
}