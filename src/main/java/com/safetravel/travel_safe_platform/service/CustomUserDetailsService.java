package com.safetravel.travel_safe_platform.service;

import com.safetravel.travel_safe_platform.entity.User;
import com.safetravel.travel_safe_platform.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Spring Security용 사용자 정보 로더.
 * <p>DB의 {@link User}를 {@link UserDetails}로 변환합니다.</p>
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 아이디로 회원을 조회해 Spring Security 사용자 객체로 반환합니다.
     *
     * @param username 로그인 아이디
     * @return UserDetails
     * @throws UsernameNotFoundException 해당 아이디가 없을 때
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다: " + username));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles("USER")
                .build();
    }
}
