
package com.safetravel.travel_safe_platform.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 지역 안전 리포트 API 응답 DTO.
 * <p>범죄 통계·치안 지표·지역 유형 설명을 담습니다.</p>
 */
@Getter
@Setter
public class RegionReportResponseDto {

    /** 지역 ID */
    private Long id;

    /** 시·도 */
    private String sido;
    /** 시·군·구 */
    private String sigungu;

    /** 강도 발생 건수 */
    private Integer robbery;
    /** 살인 발생 건수 */
    private Integer murder;
    /** 성폭력 발생 건수 */
    private Integer sexualAssault;
    /** 절도 발생 건수 */
    private Integer theft;
    /** 폭력 발생 건수 */
    private Integer violence;

    /** 종합 위험도 점수 */
    private Double riskScore;

    /** CCTV 수 */
    private Integer cctvCount;

    /** 경찰서 + 파출소 합계 */
    private Integer policeCount;

    /** data.csv 기준 지역 유형 */
    private String regionType;

    /** 지역 유형 안내 문구 */
    private String regionTypeDescription;
}

