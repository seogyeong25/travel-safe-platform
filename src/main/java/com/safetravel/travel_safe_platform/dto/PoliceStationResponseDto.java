package com.safetravel.travel_safe_platform.dto;

import com.safetravel.travel_safe_platform.entity.PoliceStation;
import lombok.Getter;

/**
 * 경찰 시설 API·지도 응답 DTO.
 */
@Getter
public class PoliceStationResponseDto {

    /** 시설 ID */
    private final Long id;
    /** 시설명 */
    private final String name;
    /** 종류 (경찰서 / 지구대 / 파출소) */
    private final String type;
    /** 주소 */
    private final String address;
    /** 위도 */
    private final Double latitude;
    /** 경도 */
    private final Double longitude;

    /**
     * {@link PoliceStation} 엔티티로부터 DTO를 구성합니다.
     *
     * @param station 경찰 시설 엔티티
     */
    public PoliceStationResponseDto(PoliceStation station) {
        this.id = station.getId();
        this.name = station.getName();
        this.type = station.getType();
        this.address = station.getAddress();
        this.latitude = station.getLatitude();
        this.longitude = station.getLongitude();
    }
}
