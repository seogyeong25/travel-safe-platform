package com.safetravel.travel_safe_platform.repository;

import com.safetravel.travel_safe_platform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * {@link User} 엔티티용 JPA 리포지토리.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /** 아이디로 회원 조회 */
    Optional<User> findByUsername(String username);

    /** 아이디 중복 여부 */
    boolean existsByUsername(String username);

    /** 이메일 중복 여부 */
    boolean existsByEmail(String email);
}
