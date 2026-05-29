package com.safetravel.travel_safe_platform.controller;

import com.safetravel.travel_safe_platform.dto.MapRegionResponseDto;
import com.safetravel.travel_safe_platform.dto.MapReportMarkerDto;
import com.safetravel.travel_safe_platform.dto.PoliceStationResponseDto;
import com.safetravel.travel_safe_platform.entity.Region;
import com.safetravel.travel_safe_platform.repository.PoliceStationRepository;
import com.safetravel.travel_safe_platform.repository.RegionRepository;
import com.safetravel.travel_safe_platform.repository.ReportRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 지도 화면용 지역·경찰 시설·게시글 REST API 컨트롤러.
 */
@RestController
@RequestMapping("/api/map")
public class MapApiController {

    private final RegionRepository regionRepository;
    private final PoliceStationRepository policeStationRepository;
    private final ReportRepository reportRepository;

    public MapApiController(
            RegionRepository regionRepository,
            PoliceStationRepository policeStationRepository,
            ReportRepository reportRepository
    ) {
        this.regionRepository = regionRepository;
        this.policeStationRepository = policeStationRepository;
        this.reportRepository = reportRepository;
    }

    /**
     * 지역 ID에 대한 지도 마커·치안 시설·게시글 데이터를 반환합니다.
     */
    @GetMapping("/regions/{id}")
    public MapRegionResponseDto getRegionMapData(@PathVariable Long id) {
        Region region = regionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("지역 없음"));

        var policeStations = policeStationRepository
                .findByRegion_IdAndLatitudeIsNotNullAndLongitudeIsNotNull(id)
                .stream()
                .map(PoliceStationResponseDto::new)
                .toList();

        var reports = reportRepository.findByRegion_IdOrderByCreatedAtDesc(id)
                .stream()
                .map(MapReportMarkerDto::new)
                .toList();

        return new MapRegionResponseDto(region, policeStations, reports);
    }
}
