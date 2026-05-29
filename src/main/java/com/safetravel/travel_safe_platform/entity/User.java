package com.safetravel.travel_safe_platform.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 회원 엔티티.
 * <p>서비스 로그인·게시글 작성자 정보를 저장합니다.</p>
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User {

    /** 기본 키 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 로그인 아이디 (고유) */
    @Column(nullable = false, unique = true)
    private String username;

    /** 이메일 (고유) */
    @Column(nullable = false, unique = true)
    private String email;

    /** BCrypt 등으로 암호화된 비밀번호 */
    @Column(nullable = false)
    private String password;

    /** 가입 일시 */
    private LocalDateTime createdAt;

    /** 화면 표시용 닉네임 */
    private String nickname;
}
