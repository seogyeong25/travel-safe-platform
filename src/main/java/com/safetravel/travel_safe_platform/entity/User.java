package com.safetravel.travel_safe_platform.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter // 💡 실무 팁: 엔티티의 무분별한 Setter는 데이터 변조 위험이 있어 지양하지만,
// 현재 단계나 뷰 템플릿 연동 단계에서는 유지하셔도 무방합니다. 나중에 제거하시는 것을 추천해요!
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA를 위한 기본 생성자 (접근 제어를 PROTECTED로 해서 안전성 상향)
@AllArgsConstructor // @Builder 사용을 위한 전체 필드 생성자
@Builder // ◀ 이 어노테이션이 추가되면서 UserService의 User.builder() 에러가 해결됩니다!
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 아이디
    @Column(nullable = false, unique = true)
    private String username;

    // 이메일
    @Column(nullable = false, unique = true)
    private String email;

    // 비밀번호
    @Column(nullable = false)
    private String password;

    // 생성일
    private LocalDateTime createdAt;

    // 닉네임
    private String nickname;
}