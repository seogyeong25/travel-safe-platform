package com.safetravel.travel_safe_platform.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 안전 신고·게시글 엔티티.
 * <p>작성자, 지역, 카테고리·위험도 등 게시판 글 정보를 저장합니다.</p>
 */
@Entity
@Table(name = "reports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Report {

    /** 기본 키 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 작성자 */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    /** 연관 지역 */
    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;

    /** 제목 */
    @Column(nullable = false)
    private String title;

    /** 본문 */
    @Column(columnDefinition = "TEXT")
    private String content;

    /** 카테고리 (예: theft, violence) */
    @Column(nullable = false)
    private String category;

    /** 위험도 등급 */
    private String dangerLevel;

    /** 조회수 */
    @Column(nullable = false)
    private int views = 0;

    /** 작성 일시 */
    private LocalDateTime createdAt;

}
