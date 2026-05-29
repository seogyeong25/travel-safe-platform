package com.safetravel.travel_safe_platform.config;

import com.safetravel.travel_safe_platform.entity.Region;
import com.safetravel.travel_safe_platform.entity.CrimeStat;
import com.safetravel.travel_safe_platform.entity.PoliceStation;
import com.safetravel.travel_safe_platform.repository.RegionRepository;
import com.safetravel.travel_safe_platform.repository.CrimeStatRepository;
import com.safetravel.travel_safe_platform.repository.PoliceStationRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 애플리케이션 기동 시 CSV에서 지역·범죄 통계·경찰 시설 데이터를 적재합니다.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final RegionRepository regionRepository;
    private final CrimeStatRepository crimeStatRepository;
    private final PoliceStationRepository policeStationRepository;

    public DataInitializer(
            RegionRepository regionRepository,
            CrimeStatRepository crimeStatRepository,
            PoliceStationRepository policeStationRepository
    ) {
        this.regionRepository = regionRepository;
        this.crimeStatRepository = crimeStatRepository;
        this.policeStationRepository = policeStationRepository;
    }

    /**
     * DB에 지역 데이터가 없을 때만 CSV를 읽어 초기 데이터를 삽입합니다.
     */
    @Override
    @Transactional
    public void run(String... args) throws Exception {

        // 이미 데이터 있으면 종료
        if (regionRepository.count() > 0) {
            return;
        }

        // 경찰 좌표 데이터 미리 로딩
        Map<String, List<PoliceStationRaw>> policeStationMap =
                loadPoliceStationCoordinates();

        ClassPathResource resource =
                new ClassPathResource("data.csv");

        List<CrimeStat> crimeStatList = new ArrayList<>();
        List<PoliceStation> policeStationList = new ArrayList<>();

        try (
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(
                                resource.getInputStream(),
                                StandardCharsets.UTF_8
                        )
                )
        ) {

            String line;

            // 헤더 건너뛰기
            br.readLine();

            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                List<String> tokens = parseCsvLine(line);

                if (tokens.size() < 20) {
                    continue;
                }

                String sido = tokens.get(0).trim();
                String sigungu = tokens.get(1).trim();

                Double riskScore =
                        parseDouble(tokens.get(14));

                String regionType =
                        tokens.get(16).trim();

                // =========================
                // Region 생성
                // =========================

                Region region = new Region();

                region.setSido(sido);
                region.setSigungu(sigungu);
                region.setRegionType(regionType);

                // 추가된 부분
                region.setRiskScore(riskScore);

                region.setCctvCount(
                        parseInteger(tokens.get(17))
                );

                region.setPoliceStationCount(
                        parseInteger(tokens.get(18))
                );

                region.setPoliceBoxCount(
                        parseInteger(tokens.get(19))
                );

                Region savedRegion =
                        regionRepository.save(region);

                // =========================
                // 범죄 데이터 저장
                // =========================

                crimeStatList.add(
                        createCrimeStatObj(
                                savedRegion,
                                "ROBBERY",
                                parseInteger(tokens.get(2)),
                                parseDouble(tokens.get(11)),
                                riskScore
                        )
                );

                crimeStatList.add(
                        createCrimeStatObj(
                                savedRegion,
                                "MURDER",
                                parseInteger(tokens.get(3)),
                                parseDouble(tokens.get(12)),
                                riskScore
                        )
                );

                crimeStatList.add(
                        createCrimeStatObj(
                                savedRegion,
                                "SEXUAL_ASSAULT",
                                parseInteger(tokens.get(4)),
                                parseDouble(tokens.get(10)),
                                riskScore
                        )
                );

                crimeStatList.add(
                        createCrimeStatObj(
                                savedRegion,
                                "THEFT",
                                parseInteger(tokens.get(5)),
                                parseDouble(tokens.get(7)),
                                riskScore
                        )
                );

                crimeStatList.add(
                        createCrimeStatObj(
                                savedRegion,
                                "VIOLENCE",
                                parseInteger(tokens.get(6)),
                                parseDouble(tokens.get(8)),
                                riskScore
                        )
                );

                // =========================
                // 경찰서 / 파출소 저장
                // =========================

                String key = sido + "_" + sigungu;

                if (policeStationMap.containsKey(key)) {

                    List<PoliceStationRaw> rawStations =
                            policeStationMap.get(key);

                    for (PoliceStationRaw raw : rawStations) {

                        PoliceStation ps =
                                new PoliceStation();

                        ps.setRegion(savedRegion);

                        ps.setName(
                                raw.name + " " + raw.type
                        );

                        ps.setType(raw.type);

                        ps.setAddress(
                                sido + " " + sigungu
                        );

                        ps.setLongitude(raw.longitude);

                        ps.setLatitude(raw.latitude);

                        policeStationList.add(ps);
                    }
                }
            }

            // =========================
            // 전체 저장
            // =========================

            if (!crimeStatList.isEmpty()) {
                crimeStatRepository.saveAll(crimeStatList);
            }

            if (!policeStationList.isEmpty()) {
                policeStationRepository.saveAll(policeStationList);
            }
        }

        System.out.println(
                "====== 📊 데이터 초기화 완료 ======"
        );
    }

    // =========================
    // 경찰 좌표 CSV 로딩
    // =========================

    private Map<String, List<PoliceStationRaw>>
    loadPoliceStationCoordinates() {

        Map<String, List<PoliceStationRaw>> map =
                new HashMap<>();

        ClassPathResource resource =
                new ClassPathResource("police.csv");

        try (
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(
                                resource.getInputStream(),
                                StandardCharsets.UTF_8
                        )
                )
        ) {

            String line;

            br.readLine();

            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                List<String> tokens =
                        parseCsvLine(line);

                if (tokens.size() < 6) {
                    continue;
                }

                String sido = tokens.get(0).trim();
                String sigungu = tokens.get(1).trim();

                String name = tokens.get(2).trim();
                String type = tokens.get(3).trim();

                Double longitude =
                        parseDouble(tokens.get(4));

                Double latitude =
                        parseDouble(tokens.get(5));

                PoliceStationRaw raw =
                        new PoliceStationRaw(
                                name,
                                type,
                                longitude,
                                latitude
                        );

                String key = sido + "_" + sigungu;

                map.computeIfAbsent(
                        key,
                        k -> new ArrayList<>()
                ).add(raw);
            }

        } catch (Exception e) {

            System.out.println(
                    "⚠️ 경찰 좌표 파일 로드 실패: "
                            + e.getMessage()
            );
        }

        return map;
    }

    // =========================
    // CSV 파싱
    // =========================

    private List<String> parseCsvLine(String line) {

        List<String> tokens =
                new ArrayList<>();

        StringBuilder sb =
                new StringBuilder();

        boolean inQuotes = false;

        for (char c : line.toCharArray()) {

            if (c == '"') {

                inQuotes = !inQuotes;

            } else if (c == ',' && !inQuotes) {

                tokens.add(
                        sb.toString()
                                .replace("\"", "")
                                .trim()
                );

                sb.setLength(0);

            } else {

                sb.append(c);
            }
        }

        tokens.add(
                sb.toString()
                        .replace("\"", "")
                        .trim()
        );

        return tokens;
    }

    // =========================
    // Integer 변환
    // =========================

    private int parseInteger(String input) {

        if (input == null || input.trim().isEmpty()) {
            return 0;
        }

        try {

            return (int) Double.parseDouble(
                    input.trim().replace(",", "")
            );

        } catch (NumberFormatException e) {

            return 0;
        }
    }

    // =========================
    // Double 변환
    // =========================

    private Double parseDouble(String input) {

        if (input == null || input.trim().isEmpty()) {
            return 0.0;
        }

        try {

            return Double.parseDouble(
                    input.trim().replace(",", "")
            );

        } catch (NumberFormatException e) {

            return 0.0;
        }
    }

    // =========================
    // CrimeStat 생성
    // =========================

    private CrimeStat createCrimeStatObj(
            Region region,
            String type,
            int count,
            Double ratio,
            Double riskScore
    ) {

        CrimeStat stat = new CrimeStat();

        stat.setRegion(region);

        stat.setCrimeType(type);

        stat.setCrimeCount(count);

        stat.setCrimeRatio(ratio);

        stat.setRiskScore(riskScore);

        stat.setStatYear(2026);

        return stat;
    }

    // =========================
    // 내부 DTO
    // =========================

    private static class PoliceStationRaw {

        String name;
        String type;
        Double longitude;
        Double latitude;

        public PoliceStationRaw(
                String name,
                String type,
                Double longitude,
                Double latitude
        ) {
            this.name = name;
            this.type = type;
            this.longitude = longitude;
            this.latitude = latitude;
        }
    }
}
