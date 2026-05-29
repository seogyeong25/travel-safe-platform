package com.safetravel.travel_safe_platform.util;

/**
 * 지역 유형(region_type)에 대한 사용자 안내 문구 유틸리티.
 */
public final class RegionTypeDescriptions {

    private RegionTypeDescriptions() {
    }

    /**
     * 지역 유형 코드에 맞는 설명 문구를 반환합니다.
     *
     * @param regionType data.csv 기준 지역 유형 문자열
     * @return 안내 문구
     */
    public static String describe(String regionType) {
        if (regionType == null || regionType.isBlank()) {
            return "이 지역의 유형 정보가 아직 등록되지 않았습니다.";
        }

        return switch (regionType.trim()) {
            case "범죄 집중 관리 지역" ->
                    "범죄 발생 건수와 위험도가 상대적으로 높은 지역입니다. "
                            + "야간 이동 시 주변을 살피고, 혼잡한 골목·유흥가는 특히 주의하세요. "
                            + "CCTV와 경찰서·파출소 위치를 미리 확인하는 것이 좋습니다.";

            case "생활 안전 지역" ->
                    "전반적으로 안정적인 생활 치안 수준을 보이는 지역입니다. "
                            + "일상적인 여행·이동에는 비교적 안전하지만, "
                            + "소지품 관리와 기본적인 안전 수칙은 지켜 주세요.";

            case "치안 인프라 보완 필요 지역" ->
                    "범죄 발생 규모는 크지 않을 수 있으나 CCTV·경찰 시설 등 "
                            + "치안 인프라가 상대적으로 부족한 지역입니다. "
                            + "이동 경로를 미리 정하고, 가능하면 밝고 사람이 많은 길을 이용하세요.";

            default ->
                    regionType + "으로 분류된 지역입니다. "
                            + "아래 범죄 통계와 치안 시설 지도를 참고해 여행 계획을 세워보세요.";
        };
    }
}
