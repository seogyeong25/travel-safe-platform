package com.safetravel.travel_safe_platform.dto;

import com.safetravel.travel_safe_platform.entity.Region;
import lombok.Getter;

import java.util.List;

/**
 * 지도 화면용 지역 종합 응답 DTO.
 */
@Getter
public class MapRegionResponseDto {

    /** 지역 ID */
    private final Long regionId;
    /** 시·도 */
    private final String sido;
    /** 시·군·구 */
    private final String sigungu;
    /** 지역 표시 라벨 */
    private final String regionLabel;
    /** 지역 유형 */
    private final String regionType;
    /** 위험도 점수 */
    private final Double riskScore;
    /** CCTV 수 */
    private final Integer cctvCount;
    /** 경찰 시설 합계 (경찰서 + 파출소) */
    private final Integer policeCount;
    /** 해당 지역 게시글 수 */
    private final int reportCount;
    /** 경찰 시설 목록 */
    private final List<PoliceStationResponseDto> policeStations;
    /** 게시글 마커 목록 */
    private final List<MapReportMarkerDto> reports;

    /**
     * 지역·경찰 시설·게시글 정보로 지도 응답을 구성합니다.
     *
     * @param region         지역 엔티티
     * @param policeStations 경찰 시설 DTO 목록
     * @param reports        게시글 마커 목록
     */
    public MapRegionResponseDto(
            Region region,
            List<PoliceStationResponseDto> policeStations,
            List<MapReportMarkerDto> reports
    ) {
        this.regionId = region.getId();
        this.sido = region.getSido();
        this.sigungu = region.getSigungu();
        this.regionLabel = region.getSido() + " " + region.getSigungu();
        this.regionType = region.getRegionType();
        this.riskScore = region.getRiskScore();
        this.cctvCount = region.getCctvCount();
        this.policeCount =
                safeInt(region.getPoliceStationCount())
                        + safeInt(region.getPoliceBoxCount());
        this.reportCount = reports.size();
        this.policeStations = policeStations;
        this.reports = reports;
    }

    private int safeInt(Integer value) {
        return value == null ? 0 : value;
    }
}
