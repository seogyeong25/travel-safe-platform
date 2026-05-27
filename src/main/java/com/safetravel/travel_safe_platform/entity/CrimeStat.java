package com.safetravel.travel_safe_platform.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "crime_stats")
@Getter
@Setter
public class CrimeStat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 지역 정보
    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;

    // 범죄 유형
    @Column(nullable = false)
    private String crimeType;

    // 범죄 발생 건수
    private Integer crimeCount;

    // 시간대
    private String timeZone;

    // 연도
    private Integer statYear;

    // 위험도 점수
    private Double riskScore;

    // ⬇️ 머신러닝 엔지니어분이 가공해 준 '범죄 비율'을 저장하기 위해 이 줄을 추가해 주세요!
    private Double crimeRatio;
}