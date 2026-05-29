package com.safetravel.travel_safe_platform.service;

import com.safetravel.travel_safe_platform.entity.User;
import com.safetravel.travel_safe_platform.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Spring Security 컨텍스트에서 현재 로그인 사용자를 조회합니다.
 */
@Service
public class CurrentUserService {

    private final UserRepository userRepository;

    public CurrentUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 로그인된 회원을 반환합니다. 미인증 시 예외를 던집니다.
     *
     * @return 현재 회원 엔티티
     * @throws AccessDeniedException 로그인되지 않은 경우
     */
    public User getCurrentUser() {
        return getCurrentUserOptional()
                .orElseThrow(() -> new AccessDeniedException("로그인이 필요합니다."));
    }

    /**
     * 로그인된 회원을 Optional로 반환합니다.
     *
     * @return 회원이 있으면 Optional, 없으면 empty
     */
    public Optional<User> getCurrentUserOptional() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return Optional.empty();
        }
        Object principal = auth.getPrincipal();
        if (principal == null || "anonymousUser".equals(principal)) {
            return Optional.empty();
        }
        return userRepository.findByUsername(auth.getName());
    }
}
