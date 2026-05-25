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
}