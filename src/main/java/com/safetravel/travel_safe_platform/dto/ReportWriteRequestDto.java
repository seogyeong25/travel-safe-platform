package com.safetravel.travel_safe_platform.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportWriteRequestDto {

    private String title;

    private String content;

    private String dangerLevel;

    private String category;
}