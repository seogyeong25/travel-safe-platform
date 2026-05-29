package com.safetravel.travel_safe_platform.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 게시글·신고 작성 요청 DTO.
 */
@Getter
@Setter
public class ReportWriteRequestDto {

    /** 제목 */
    private String title;

    /** 본문 */
    private String content;

    /** 위험도 */
    private String dangerLevel;

    /** 카테고리 */
    private String category;

    /** 연관 지역 ID */
    private Long regionId;
}
