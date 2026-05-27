package com.safetravel.travel_safe_platform.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "regions")
@Getter
@Setter
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 시/도
    @Column(nullable = false)
    private String sido;

    // 시/군/구
    @Column(nullable = false)
    private String sigungu;

    // ⬇️ 머신러닝 군집 결과 분류를 저장하기 위해 이 한 줄만 추가해 주세요!
    private String regionType;
}