
package com.safetravel.travel_safe_platform.controller;

import com.safetravel.travel_safe_platform.dto.PoliceStationResponseDto;
import com.safetravel.travel_safe_platform.dto.RegionReportResponseDto;
import com.safetravel.travel_safe_platform.dto.ReportResponseDto;
import com.safetravel.travel_safe_platform.util.RegionTypeDescriptions;
import com.safetravel.travel_safe_platform.entity.CrimeStat;
import com.safetravel.travel_safe_platform.entity.Region;
import com.safetravel.travel_safe_platform.repository.CrimeStatRepository;
import com.safetravel.travel_safe_platform.repository.RegionRepository;
import com.safetravel.travel_safe_platform.repository.PoliceStationRepository;
import com.safetravel.travel_safe_platform.repository.ReportRepository;

import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 지역 안전 리포트·관련 데이터 REST API 컨트롤러.
 */
@RestController
@RequestMapping("/api/reports")
public class ReportApiController {

    private final RegionRepository regionRepository;
    private final CrimeStatRepository crimeStatRepository;
    private final ReportRepository reportRepository;
    private final PoliceStationRepository policeStationRepository;

    public ReportApiController(
            RegionRepository regionRepository,
            CrimeStatRepository crimeStatRepository,
            ReportRepository reportRepository,
            PoliceStationRepository policeStationRepository
    ) {
        this.regionRepository = regionRepository;
        this.crimeStatRepository = crimeStatRepository;
        this.reportRepository = reportRepository;
        this.policeStationRepository = policeStationRepository;
    }

    /**
     * 지역 ID에 대한 범죄 통계·치안 지표를 반환합니다.
     */
    @GetMapping("/{id}")
    public RegionReportResponseDto getReport(
            @PathVariable Long id
    ) {

        Region region = regionRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("지역 없음")
                );

        List<CrimeStat> stats =
                crimeStatRepository.findByRegionId(id);

        RegionReportResponseDto dto =
                new RegionReportResponseDto();

        dto.setId(region.getId());

        dto.setSido(region.getSido());
        dto.setSigungu(region.getSigungu());

        dto.setRiskScore(region.getRiskScore());

        dto.setCctvCount(region.getCctvCount());

        dto.setPoliceCount(
                region.getPoliceStationCount()
                        + region.getPoliceBoxCount()
        );

        dto.setRegionType(region.getRegionType());
        dto.setRegionTypeDescription(
                RegionTypeDescriptions.describe(region.getRegionType())
        );

        for (CrimeStat stat : stats) {

            switch (stat.getCrimeType()) {

                case "ROBBERY":
                    dto.setRobbery(stat.getCrimeCount());
                    break;

                case "MURDER":
                    dto.setMurder(stat.getCrimeCount());
                    break;

                case "SEXUAL_ASSAULT":
                    dto.setSexualAssault(stat.getCrimeCount());
                    break;

                case "THEFT":
                    dto.setTheft(stat.getCrimeCount());
                    break;

                case "VIOLENCE":
                    dto.setViolence(stat.getCrimeCount());
                    break;
            }
        }

        return dto;
    }

    /**
     * 해당 지역의 최신 게시글 5건을 반환합니다.
     */
    @GetMapping("/{id}/posts")
    public List<ReportResponseDto> getRegionPosts(
            @PathVariable Long id
    ) {
        return reportRepository.findTop5ByRegion_IdOrderByCreatedAtDesc(id)
                .stream()
                .map(ReportResponseDto::new)
                .toList();
    }

    /**
     * 해당 지역의 경찰 시설(좌표 있음) 목록을 반환합니다.
     */
    @GetMapping("/{id}/police-stations")
    public List<PoliceStationResponseDto> getPoliceStations(
            @PathVariable Long id
    ) {
        regionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("지역 없음"));

        return policeStationRepository
                .findByRegion_IdAndLatitudeIsNotNullAndLongitudeIsNotNull(id)
                .stream()
                .map(PoliceStationResponseDto::new)
                .toList();
    }
}

