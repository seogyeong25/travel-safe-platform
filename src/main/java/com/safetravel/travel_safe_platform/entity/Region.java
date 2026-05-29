package com.safetravel.travel_safe_platform.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 행정 지역 엔티티.
 * <p>시·도·시군구, 치안 지표(CCTV, 경찰 시설), 범죄 통계 목록을 보유합니다.</p>
 */
@Entity
@Table(name = "regions")
@Getter
@Setter
public class Region {

    /** 기본 키 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 시·도명 */
    @Column(nullable = false)
    private String sido;

    /** 시·군·구명 */
    @Column(nullable = false)
    private String sigungu;

    /** 지역 유형 (예: 생활 안전 지역) */
    @Column(name = "region_type")
    private String regionType;

    /** 종합 위험도 점수 */
    @Column(name = "risk_score")
    private Double riskScore;

    /** CCTV 설치 대수 */
    @Column(name = "cctv_count")
    private Integer cctvCount;

    /** 경찰서 수 */
    @Column(name = "police_station_count")
    private Integer policeStationCount;

    /** 파출소 수 */
    @Column(name = "police_box_count")
    private Integer policeBoxCount;

    /** 해당 지역의 범죄 통계 목록 */
    @OneToMany(
            mappedBy = "region",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<CrimeStat> crimeStats;
}

