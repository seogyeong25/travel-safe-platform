package com.safetravel.travel_safe_platform.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 작성자
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // 지역
    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;

    // 제목
    @Column(nullable = false)
    private String title;

    // 내용
    @Column(columnDefinition = "TEXT")
    private String content;

    // 카테고리
    private String category;

    // 위험도
    private String dangerLevel;

    // 조회수
    private int views;

    // 작성일
    private LocalDateTime createdAt;
}