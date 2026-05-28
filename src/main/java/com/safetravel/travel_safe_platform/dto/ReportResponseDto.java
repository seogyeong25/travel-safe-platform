package com.safetravel.travel_safe_platform.dto;

import com.safetravel.travel_safe_platform.entity.Report;
import java.time.format.DateTimeFormatter;
import lombok.Getter;

@Getter
public class ReportResponseDto {

    private Long id;

    private String title;

    private String content;

    private String dangerLevel;

    private String category;

    private String nickname;

    private String regionName;

    private String createdAt;

    private int views;

    // 생성자
    public ReportResponseDto(Report report) {

        this.id = report.getId();

        this.title = report.getTitle();

        this.content = report.getContent();

        this.dangerLevel = report.getDangerLevel();

        this.category = report.getCategory();

        // 작성자 닉네임
        this.nickname = report.getUser().getNickname();

        // 지역명
        this.regionName = report.getRegion().getSigungu();

        // 날짜
        this.createdAt = report.getCreatedAt()
                .format(
                        DateTimeFormatter.ofPattern(
                                "yyyy-MM-dd HH:mm"
                        )
                );

        // 조회수
        this.views = report.getViews();
    }
}