package com.safetravel.travel_safe_platform.dto;

import com.safetravel.travel_safe_platform.entity.Region;
import lombok.Getter;

import java.util.List;

/**
 * 지역 키워드 검색 API 응답 DTO.
 * <p>단일 매칭, 다중 후보, 미발견 등 검색 결과 유형을 표현합니다.</p>
 */
@Getter
public class RegionSearchResponseDto {

    /** 검색 성공(단일 지역 확정) 여부 */
    private final boolean found;
    /** 후보가 여러 개인지 여부 */
    private final boolean multiple;
    /** 리다이렉트 URL (단일 매칭·미발견 시) */
    private final String redirectUrl;
    /** 사용자 안내 메시지 */
    private final String message;
    /** 다중 후보 목록 */
    private final List<RegionCandidateDto> candidates;

    private RegionSearchResponseDto(
            boolean found,
            boolean multiple,
            String redirectUrl,
            String message,
            List<RegionCandidateDto> candidates
    ) {
        this.found = found;
        this.multiple = multiple;
        this.redirectUrl = redirectUrl;
        this.message = message;
        this.candidates = candidates;
    }

    /** 단일 지역이 확정된 응답 */
    public static RegionSearchResponseDto single(Region region) {
        return new RegionSearchResponseDto(
                true,
                false,
                "/report?regionId=" + region.getId(),
                null,
                List.of()
        );
    }

    /** 여러 후보가 있는 응답 */
    public static RegionSearchResponseDto multiple(List<Region> regions) {
        List<RegionCandidateDto> candidates = regions.stream()
                .map(RegionCandidateDto::new)
                .toList();

        return new RegionSearchResponseDto(
                false,
                true,
                null,
                "여러 지역이 검색되었습니다. 원하는 지역을 선택해주세요.",
                candidates
        );
    }

    /** 검색 결과 없음 */
    public static RegionSearchResponseDto notFound(String keyword) {
        String encoded = keyword == null ? "" : keyword.trim();

        return new RegionSearchResponseDto(
                false,
                false,
                "/region?keyword=" + java.net.URLEncoder.encode(encoded, java.nio.charset.StandardCharsets.UTF_8),
                "'" + encoded + "'에 맞는 지역을 찾지 못했습니다. 지역 선택 페이지에서 고를 수 있습니다.",
                List.of()
        );
    }

    /** 잘못된 요청(빈 검색어 등) */
    public static RegionSearchResponseDto badRequest(String message) {
        return new RegionSearchResponseDto(
                false,
                false,
                "/region",
                message,
                List.of()
        );
    }

    /**
     * 검색 후보 한 건.
     */
    @Getter
    public static class RegionCandidateDto {
        /** 지역 ID */
        private final Long id;
        /** 표시 라벨 */
        private final String label;
        /** 선택 시 이동 URL */
        private final String redirectUrl;

        public RegionCandidateDto(Region region) {
            this.id = region.getId();
            this.label = region.getSido() + " " + region.getSigungu();
            this.redirectUrl = "/report?regionId=" + region.getId();
        }
    }
}
