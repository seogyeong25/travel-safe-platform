package com.safetravel.travel_safe_platform.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "reports")
@Getter
@Setter
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
    @JoinColumn(name = "region_id")  // DB에서 region_id 컬럼으로 연결
    private Region region;

    // 제목
    @Column(nullable = false)
    private String title;

    // 내용
    @Column(columnDefinition = "TEXT")
    private String content;

    // 위도
    private Double latitude;

    // 경도
    private Double longitude;

    // 위험도
    private String dangerLevel;

    // 작성일
    private LocalDateTime createdAt;
}