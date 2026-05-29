package com.safetravel.travel_safe_platform.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * 지역별 범죄 통계 엔티티.
 * <p>범죄 유형별 발생 건수·비율·위험도를 저장합니다.</p>
 */
@Entity
@Table(name = "crime_stats")
@Getter
@Setter
public class CrimeStat {

    /** 기본 키 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 소속 지역 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;

    /**
     * 범죄 유형 코드.
     * <p>예: ROBBERY, MURDER, SEXUAL_ASSAULT, THEFT, VIOLENCE</p>
     */
    @Column(nullable = false)
    private String crimeType;

    /** 해당 유형 범죄 발생 건수 */
    @Column(name = "crime_count")
    private Integer crimeCount;

    /** 시간대 (선택) */
    private String timeZone;

    /** 통계 기준 연도 */
    private Integer statYear;

    /** 위험도 점수 */
    @Column(name = "risk_score")
    private Double riskScore;

    /** 범죄 비율 */
    @Column(name = "crime_ratio")
    private Double crimeRatio;
}

