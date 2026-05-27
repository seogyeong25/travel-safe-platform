package com.safetravel.travel_safe_platform.dto;

import com.safetravel.travel_safe_platform.entity.CrimeStat;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class RegionCrimeResponse {
    private String sido;
    private String sigungu;

    // 5대 범죄 수치
    private int robbery;       // 강도
    private int murder;        // 살인
    private int sexualAssault; // 성범죄
    private int theft;         // 절도
    private int violence;      // 폭력

    // 치안 인프라
    private int cctvCount;

    public RegionCrimeResponse(String sido, String sigungu, List<CrimeStat> stats) {
        this.sido = sido;
        this.sigungu = sigungu;

        // 리스트로 들어온 여러 행의 CrimeStat을 종류별로 매핑
        for (CrimeStat stat : stats) {
            switch (stat.getCrimeType()) {
                case "강도": this.robbery = stat.getCrimeCount(); break;
                case "살인": this.murder = stat.getCrimeCount(); break;
                case "성범죄": this.sexualAssault = stat.getCrimeCount(); break;
                case "절도": this.theft = stat.getCrimeCount(); break;
                case "폭력": this.violence = stat.getCrimeCount(); break;
                case "CCTV": this.cctvCount = stat.getCrimeCount(); break;
            }
        }
    }
}