package com.safetravel.travel_safe_platform.dto;

import com.safetravel.travel_safe_platform.entity.Report;
import com.safetravel.travel_safe_platform.entity.Region;
import java.time.format.DateTimeFormatter;
import lombok.Getter;

/**
 * 게시글·신고 상세/목록 응답 DTO.
 */
@Getter
public class ReportResponseDto {

    /** 게시글 ID */
    private Long id;

    /** 제목 */
    private String title;

    /** 본문 */
    private String content;

    /** 위험도 */
    private String dangerLevel;

    /** 카테고리 */
    private String category;

    /** 작성자 닉네임 */
    private String nickname;

    /** 지역 ID */
    private Long regionId;

    /** 시·도 */
    private String sido;

    /** 시·군·구 */
    private String sigungu;

    /** 지역 표시명 (시도 + 시군구) */
    private String regionName;

    /** 작성 일시 (포맷 문자열) */
    private String createdAt;

    /** 조회수 */
    private int views;

    /** 작성자 회원 ID */
    private Long authorId;

    /**
     * {@link Report} 엔티티로부터 응답 DTO를 구성합니다.
     *
     * @param report 게시글 엔티티
     */
    public ReportResponseDto(Report report) {

        this.id = report.getId();

        this.title = report.getTitle();

        this.content = report.getContent();

        this.dangerLevel = report.getDangerLevel();

        this.category = report.getCategory();

        if (report.getUser() != null) {
            this.nickname = report.getUser().getNickname();
            this.authorId = report.getUser().getId();
        }

        Region region = report.getRegion();

        if (region != null) {
            this.regionId = region.getId();
            this.sido = region.getSido();
            this.sigungu = region.getSigungu();
            this.regionName = region.getSido() + " " + region.getSigungu();
        }

        if (report.getCreatedAt() != null) {
            this.createdAt = report.getCreatedAt()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        }

        this.views = report.getViews();
    }
}
