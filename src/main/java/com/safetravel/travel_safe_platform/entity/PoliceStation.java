package com.safetravel.travel_safe_platform.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "police_stations")
@Getter
@Setter
public class PoliceStation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 지역
    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;

    // 경찰서 이름
    private String name;

    // 주소
    private String address;

    // 위도
    private Double latitude;

    // 경도
    private Double longitude;

    // 종류
    // 경찰서 / 지구대 / 파출소
    private String type;
}