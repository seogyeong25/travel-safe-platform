package com.safetravel.travel_safe_platform.dto;

import com.safetravel.travel_safe_platform.entity.Report;
import lombok.Getter;

/**
 * 지도 마커용 게시글 요약 DTO.
 */
@Getter
public class MapReportMarkerDto {

    /** 게시글 ID */
    private final Long id;
    /** 제목 */
    private final String title;
    /** 카테고리 */
    private final String category;
    /** 위험도 */
    private final String dangerLevel;
    /** 본문 요약용 내용 */
    private final String content;

    /**
     * {@link Report} 엔티티로부터 마커 DTO를 구성합니다.
     *
     * @param report 게시글 엔티티
     */
    public MapReportMarkerDto(Report report) {
        this.id = report.getId();
        this.title = report.getTitle();
        this.category = report.getCategory();
        this.dangerLevel = report.getDangerLevel();
        this.content = report.getContent();
    }
}
