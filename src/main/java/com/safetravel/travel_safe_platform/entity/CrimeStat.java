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
}