package com.safetravel.travel_safe_platform.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * 경찰 시설 엔티티.
 * <p>경찰서·지구대·파출소의 위치·주소 정보를 저장합니다.</p>
 */
@Entity
@Table(name = "police_stations")
@Getter
@Setter
public class PoliceStation {

    /** 기본 키 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 소속 지역 */
    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;

    /** 시설명 */
    private String name;

    /** 주소 */
    private String address;

    /** 위도 */
    private Double latitude;

    /** 경도 */
    private Double longitude;

    /** 시설 종류 (경찰서 / 지구대 / 파출소) */
    private String type;
}
