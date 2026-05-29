package com.safetravel.travel_safe_platform.config;

import com.safetravel.travel_safe_platform.entity.Region;
import com.safetravel.travel_safe_platform.entity.RegionAlias;
import com.safetravel.travel_safe_platform.repository.RegionAliasRepository;
import com.safetravel.travel_safe_platform.repository.RegionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 지역 검색 별칭(홍대, 강남 등) 시드 데이터를 등록합니다.
 */
@Component
@Order(2)
public class RegionAliasInitializer implements CommandLineRunner {

    private final RegionRepository regionRepository;
    private final RegionAliasRepository regionAliasRepository;

    public RegionAliasInitializer(
            RegionRepository regionRepository,
            RegionAliasRepository regionAliasRepository
    ) {
        this.regionRepository = regionRepository;
        this.regionAliasRepository = regionAliasRepository;
    }

    /**
     * 지역 데이터가 존재할 때 대표 별칭 키워드를 DB에 저장합니다.
     */
    @Override
    @Transactional
    public void run(String... args) {
        if (regionRepository.count() == 0) {
            return;
        }

        seed("홍대", "서울", "마포구");
        seed("홍대입구", "서울", "마포구");
        seed("해운대", "부산", "해운대구");
        seed("해운대해수욕장", "부산", "해운대구");
        seed("제주도", "제주", "제주시");
        seed("제주", "제주", "제주시");
        seed("강릉", "강원", "강릉시");
        seed("경주", "경북", "경주시");
        seed("부산", "부산", "해운대구");
        seed("마포", "서울", "마포구");
        seed("강남", "서울", "강남구");
        seed("명동", "서울", "중구");
    }

    private void seed(String keyword, String sido, String sigungu) {
        String aliasKey = normalizeKeyword(keyword);
        if (regionAliasRepository.findByKeyword(aliasKey).isPresent()) {
            return;
        }

        regionRepository.findBySidoAndSigungu(sido, sigungu).ifPresent(region -> {
            RegionAlias alias = new RegionAlias();
            alias.setKeyword(aliasKey);
            alias.setRegion(region);
            regionAliasRepository.save(alias);
        });
    }

    private String normalizeKeyword(String keyword) {
        return keyword.trim().toLowerCase().replaceAll("\\s+", "");
    }
}
