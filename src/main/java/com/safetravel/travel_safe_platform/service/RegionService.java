package com.safetravel.travel_safe_platform.service;

import com.safetravel.travel_safe_platform.dto.RegionSearchResponseDto;
import com.safetravel.travel_safe_platform.entity.Region;
import com.safetravel.travel_safe_platform.entity.RegionAlias;
import com.safetravel.travel_safe_platform.repository.RegionAliasRepository;
import com.safetravel.travel_safe_platform.repository.RegionRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 지역 조회·키워드 검색 비즈니스 로직.
 */
@Service
public class RegionService {

    private static final int MAX_CANDIDATES = 8;

    private final RegionRepository regionRepository;
    private final RegionAliasRepository regionAliasRepository;

    public RegionService(
            RegionRepository regionRepository,
            RegionAliasRepository regionAliasRepository
    ) {
        this.regionRepository = regionRepository;
        this.regionAliasRepository = regionAliasRepository;
    }

    /** 중복 제거된 시·도 목록을 반환합니다. */
    public List<String> getSidoList() {
        return regionRepository.findDistinctSido();
    }

    /** 전체 지역 목록을 반환합니다. */
    public List<Region> getAllRegions() {
        return regionRepository.findAll();
    }

    /** 시·도에 속한 시·군·구 목록을 반환합니다. */
    public List<Region> getSigunguBySido(String sido) {
        return regionRepository.findBySidoOrderBySigunguAsc(sido);
    }

    /** 시군구 키워드로 지역을 검색합니다. */
    public List<Region> searchRegions(String keyword) {
        return regionRepository.findBySigunguContaining(keyword);
    }

    /** 시·도·시군구 조건으로 지역을 검색합니다. */
    public List<Region> searchRegions(String sido, String sigungu) {
        if (sido == null || sido.isBlank()) {
            return List.of();
        }

        if (sigungu == null || sigungu.isBlank()) {
            return regionRepository.findBySidoOrderBySigunguAsc(sido);
        }

        return regionRepository.findBySidoAndSigunguContainingOrderBySigunguAsc(
                sido,
                sigungu
        );
    }

    /** ID로 지역을 조회합니다. 없으면 예외를 던집니다. */
    public Region getRegionOrThrow(Long regionId) {
        return regionRepository.findById(regionId)
                .orElseThrow(() -> new RuntimeException("지역을 선택해주세요."));
    }

    /**
     * 검색어로 지역을 찾아 단일·다중·미발견 응답을 반환합니다.
     *
     * @param keyword 사용자 입력 검색어
     * @return 검색 결과 DTO
     */
    public RegionSearchResponseDto searchByKeyword(String keyword) {
        String query = normalizeKeyword(keyword);
        if (query.isEmpty()) {
            return RegionSearchResponseDto.badRequest("검색어를 입력해주세요.");
        }

        Optional<Region> aliasMatch = findByAlias(query);
        if (aliasMatch.isPresent()) {
            return RegionSearchResponseDto.single(aliasMatch.get());
        }

        for (String variant : keywordVariants(query)) {
            RegionSearchResponseDto result = resolveVariant(variant);
            if (result != null) {
                return result;
            }
        }

        return RegionSearchResponseDto.notFound(query);
    }

    private Optional<Region> findByAlias(String query) {
        String aliasKey = toAliasKey(query);
        return regionAliasRepository.findByKeyword(aliasKey)
                .map(RegionAlias::getRegion);
    }

    private RegionSearchResponseDto resolveVariant(String query) {
        if (query == null || query.isBlank()) {
            return null;
        }
        if (query.contains(" ")) {
            Optional<Region> pair = searchBySidoAndSigunguPair(query);
            if (pair.isPresent()) {
                return RegionSearchResponseDto.single(pair.get());
            }

            List<Region> pairCandidates = findPairCandidates(query);
            if (shouldOfferMultiple(pairCandidates, query)) {
                return RegionSearchResponseDto.multiple(limit(pairCandidates));
            }
        }

        Optional<Region> exactSigungu = regionRepository.findBySigungu(query);
        if (exactSigungu.isPresent()) {
            return RegionSearchResponseDto.single(exactSigungu.get());
        }

        List<Region> sigunguMatches =
                regionRepository.findBySigunguContaining(query);

        Optional<Region> best = pickBestMatch(query, sigunguMatches);
        if (best.isPresent() && isConfidentMatch(query, best.get(), sigunguMatches)) {
            return RegionSearchResponseDto.single(best.get());
        }

        if (shouldOfferMultiple(sigunguMatches, query)) {
            return RegionSearchResponseDto.multiple(limit(sigunguMatches));
        }

        String sidoToken = stripDoSuffix(query);
        List<Region> sidoMatches =
                regionRepository.findBySidoContainingOrderBySigunguAsc(sidoToken);

        if (sidoMatches.size() == 1) {
            return RegionSearchResponseDto.single(sidoMatches.get(0));
        }

        if (shouldOfferMultiple(sidoMatches, query)) {
            return RegionSearchResponseDto.multiple(limit(sidoMatches));
        }

        List<Region> combined = regionRepository
                .findBySidoContainingOrSigunguContainingOrderBySigunguAsc(
                        sidoToken,
                        query
                );

        best = pickBestMatch(query, combined);
        if (best.isPresent() && isConfidentMatch(query, best.get(), combined)) {
            return RegionSearchResponseDto.single(best.get());
        }

        if (shouldOfferMultiple(combined, query)) {
            return RegionSearchResponseDto.multiple(limit(combined));
        }

        return null;
    }

    private Optional<Region> searchBySidoAndSigunguPair(String query) {
        String[] parts = query.split("\\s+", 2);
        String sidoPart = stripDoSuffix(parts[0]);
        String sigunguPart = parts[1];

        List<Region> matches = regionRepository
                .findBySidoAndSigunguContainingOrderBySigunguAsc(sidoPart, sigunguPart);

        return pickBestMatch(sigunguPart, matches);
    }

    private List<Region> findPairCandidates(String query) {
        String[] parts = query.split("\\s+", 2);
        String sidoPart = stripDoSuffix(parts[0]);
        String sigunguPart = parts[1];

        List<Region> matches = new ArrayList<>(
                regionRepository.findBySidoAndSigunguContainingOrderBySigunguAsc(
                        sidoPart,
                        sigunguPart
                )
        );

        matches.addAll(
                regionRepository.findBySidoAndSigunguContainingOrderBySigunguAsc(
                        parts[0],
                        sigunguPart
                )
        );

        return distinctRegions(matches);
    }

    private boolean shouldOfferMultiple(List<Region> regions, String keyword) {
        if (regions == null || regions.size() < 2) {
            return false;
        }

        List<Region> distinct = distinctRegions(regions);
        if (distinct.size() < 2) {
            return false;
        }

        Optional<Region> best = pickBestMatch(keyword, distinct);
        if (best.isEmpty()) {
            return true;
        }

        return !isConfidentMatch(keyword, best.get(), distinct);
    }

    private boolean isConfidentMatch(
            String keyword,
            Region best,
            List<Region> regions
    ) {
        if (best.getSigungu().equals(keyword)) {
            return true;
        }

        int bestScore = sigunguMatchScore(keyword, best.getSigungu());

        long sameScoreCount = regions.stream()
                .filter(r -> sigunguMatchScore(keyword, r.getSigungu()) == bestScore)
                .count();

        return sameScoreCount == 1 && bestScore <= 1;
    }

    private Optional<Region> pickBestMatch(String keyword, List<Region> regions) {
        List<Region> distinct = distinctRegions(regions);
        if (distinct.isEmpty()) {
            return Optional.empty();
        }

        if (distinct.size() == 1) {
            return Optional.of(distinct.get(0));
        }

        List<Region> exactSigungu = distinct.stream()
                .filter(r -> r.getSigungu().equals(keyword))
                .toList();

        if (exactSigungu.size() == 1) {
            return Optional.of(exactSigungu.get(0));
        }

        return distinct.stream()
                .min(Comparator
                        .comparingInt((Region r) -> sigunguMatchScore(keyword, r.getSigungu()))
                        .thenComparing(Region::getSigungu));
    }

    private List<Region> distinctRegions(List<Region> regions) {
        Map<Long, Region> map = new LinkedHashMap<>();
        for (Region region : regions) {
            map.putIfAbsent(region.getId(), region);
        }
        return new ArrayList<>(map.values());
    }

    private List<Region> limit(List<Region> regions) {
        return distinctRegions(regions).stream()
                .limit(MAX_CANDIDATES)
                .toList();
    }

    private int sigunguMatchScore(String keyword, String sigungu) {
        if (sigungu.equals(keyword)) {
            return 0;
        }
        if (sigungu.startsWith(keyword)) {
            return 1;
        }
        if (sigungu.contains(keyword)) {
            return 2;
        }
        return 3;
    }

    private List<String> keywordVariants(String query) {
        List<String> variants = new ArrayList<>();
        variants.add(query);

        String strippedAdmin = stripAdministrativeSuffix(query);
        if (!variants.contains(strippedAdmin)) {
            variants.add(strippedAdmin);
        }

        String withoutGu = stripLocalSuffix(query);
        if (!variants.contains(withoutGu)) {
            variants.add(withoutGu);
        }

        String sidoOnly = stripDoSuffix(query);
        if (!variants.contains(sidoOnly)) {
            variants.add(sidoOnly);
        }

        return variants;
    }

    private String normalizeKeyword(String keyword) {
        if (keyword == null) {
            return "";
        }
        return keyword.trim().replaceAll("\\s+", " ");
    }

    private String toAliasKey(String keyword) {
        return normalizeKeyword(keyword).toLowerCase().replaceAll("\\s+", "");
    }

    private String stripDoSuffix(String token) {
        if (token == null || token.isBlank()) {
            return "";
        }
        return token
                .replaceAll("(특별자치도|특별자치시|특별시|광역시)$", "")
                .replaceAll("도$", "")
                .trim();
    }

    private String stripAdministrativeSuffix(String token) {
        if (token == null || token.isBlank()) {
            return "";
        }
        return token
                .replaceAll("(특별자치도|특별자치시|특별시|광역시)$", "")
                .trim();
    }

    private String stripLocalSuffix(String token) {
        if (token == null || token.isBlank()) {
            return "";
        }
        String value = stripAdministrativeSuffix(token);
        if (value.matches(".*[시군구]$")) {
            return value.replaceAll("[시군구]$", "");
        }
        return value;
    }
}
