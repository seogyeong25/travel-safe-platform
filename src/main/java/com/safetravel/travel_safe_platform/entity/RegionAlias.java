package com.safetravel.travel_safe_platform.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * 지역 검색 별칭 엔티티.
 * <p>「홍대」「강남」 등 사용자 검색어를 실제 {@link Region}에 매핑합니다.</p>
 */
@Entity
@Table(name = "region_aliases")
@Getter
@Setter
public class RegionAlias {

    /** 기본 키 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 정규화된 검색 키워드 (소문자, 공백 제거) */
    @Column(nullable = false, unique = true, length = 100)
    private String keyword;

    /** 매핑 대상 지역 */
    @ManyToOne(optional = false)
    @JoinColumn(name = "region_id", nullable = false)
    private Region region;
}
