package com.safetravel.travel_safe_platform.repository;

import com.safetravel.travel_safe_platform.entity.RegionAlias;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * {@link RegionAlias} 엔티티용 JPA 리포지토리.
 */
public interface RegionAliasRepository extends JpaRepository<RegionAlias, Long> {

    /** 정규화 키워드로 별칭 조회 */
    Optional<RegionAlias> findByKeyword(String keyword);
}
