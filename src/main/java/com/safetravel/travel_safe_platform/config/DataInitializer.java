package com.safetravel.travel_safe_platform.config;

import com.safetravel.travel_safe_platform.entity.Region;
import com.safetravel.travel_safe_platform.entity.CrimeStat;
import com.safetravel.travel_safe_platform.entity.PoliceStation;
import com.safetravel.travel_safe_platform.repository.RegionRepository;
import com.safetravel.travel_safe_platform.repository.CrimeStatRepository;
import com.safetravel.travel_safe_platform.repository.PoliceStationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RegionRepository regionRepository;
    private final CrimeStatRepository crimeStatRepository;
    private final PoliceStationRepository policeStationRepository;

    public DataInitializer(RegionRepository regionRepository,
                           CrimeStatRepository crimeStatRepository,
                           PoliceStationRepository policeStationRepository) {
        this.regionRepository = regionRepository;
        this.crimeStatRepository = crimeStatRepository;
        this.policeStationRepository = policeStationRepository;
    }

    @Override
    @Transactional // Batch 처리를 위한 트랜잭션 보장
    public void run(String... args) throws Exception {
        if (regionRepository.count() > 0) return;

        // 1. 실제 경찰서/파출소 좌표 CSV 데이터를 메모리에 먼저 로드 (Map 구조 활용)
        // Key 형식을 "시도_시군구"로 지정하여 빠르게 조회할 수 있도록 구성합니다.
        Map<String, List<PoliceStationRaw>> policeStationMap = loadPoliceStationCoordinates();

        ClassPathResource resource = new ClassPathResource("data.csv");
        List<CrimeStat> crimeStatList = new ArrayList<>();
        List<PoliceStation> policeStationList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            br.readLine(); // 헤더 건너뛰기

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                List<String> tokens = parseCsvLine(line);
                if (tokens.size() < 20) continue;

                String sido = tokens.get(0).trim();
                String sigungu = tokens.get(1).trim();
                Double riskScore = parseDouble(tokens.get(14));
                String regionType = tokens.get(16).trim();

                // 2. 마스터 엔티티 Region 생성 및 즉시 저장 (ID 생성을 위해 필요)
                Region region = new Region();
                region.setSido(sido);
                region.setSigungu(sigungu);
                region.setRegionType(regionType);
                Region savedRegion = regionRepository.save(region);

                // 3. 5대 범죄 수치 및 CCTV 적재 리스트화
                crimeStatList.add(createCrimeStatObj(savedRegion, "강도", parseInteger(tokens.get(2)), parseDouble(tokens.get(11)), riskScore));
                crimeStatList.add(createCrimeStatObj(savedRegion, "살인", parseInteger(tokens.get(3)), parseDouble(tokens.get(12)), riskScore));
                crimeStatList.add(createCrimeStatObj(savedRegion, "성범죄", parseInteger(tokens.get(4)), parseDouble(tokens.get(10)), riskScore));
                crimeStatList.add(createCrimeStatObj(savedRegion, "절도", parseInteger(tokens.get(5)), parseDouble(tokens.get(7)), riskScore));
                crimeStatList.add(createCrimeStatObj(savedRegion, "폭력", parseInteger(tokens.get(6)), parseDouble(tokens.get(8)), riskScore));

                int cctvCount = parseInteger(tokens.get(17));
                crimeStatList.add(createCrimeStatObj(savedRegion, "CCTV", cctvCount, 0.0, 0.0));

                // 4. 🔥 핵심: 로드해둔 실제 좌표 데이터와 매핑하여 고유 관공서 정보 적재
                String key = sido + "_" + sigungu;
                if (policeStationMap.containsKey(key)) {
                    List<PoliceStationRaw> rawStations = policeStationMap.get(key);
                    for (PoliceStationRaw raw : rawStations) {
                        PoliceStation ps = new PoliceStation();
                        ps.setRegion(savedRegion);
                        // 이름 가공 (예: "을지" + " " + "지구대" -> "을지 지구대")
                        ps.setName(raw.name + " " + raw.type);
                        ps.setType(raw.type);
                        ps.setAddress(sido + " " + sigungu);
                        ps.setLongitude(raw.longitude);
                        ps.setLatitude(raw.latitude);
                        policeStationList.add(ps);
                    }
                }
            }

            // 전체 일괄 벌크 저장 (성능 최적화)
            if (!crimeStatList.isEmpty()) crimeStatRepository.saveAll(crimeStatList);
            if (!policeStationList.isEmpty()) policeStationRepository.saveAll(policeStationList);
        }
        System.out.println("====== 📊 [성공] 실제 좌표 데이터를 결합한 통합 데이터 적재 완료! ======");
    }

    /**
     * 경찰서/파출소 좌표 CSV(police.csv)를 미리 파싱하여 Map에 보관하는 메서드
     */
    private Map<String, List<PoliceStationRaw>> loadPoliceStationCoordinates() {
        Map<String, List<PoliceStationRaw>> map = new HashMap<>();
        ClassPathResource resource = new ClassPathResource("police.csv"); // 파일명 확인 필요

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            br.readLine(); // 헤더 건너뛰기

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                List<String> tokens = parseCsvLine(line);
                if (tokens.size() < 6) continue;

                String sido = tokens.get(0).trim();
                String sigungu = tokens.get(1).trim();
                String name = tokens.get(2).trim();
                String type = tokens.get(3).trim();
                Double longitude = parseDouble(tokens.get(4));
                Double latitude = parseDouble(tokens.get(5));

                PoliceStationRaw raw = new PoliceStationRaw(name, type, longitude, latitude);
                String key = sido + "_" + sigungu;

                map.computeIfAbsent(key, k -> new ArrayList<>()).add(raw);
            }
        } catch (Exception e) {
            System.out.println("⚠️ [경고] 경찰 좌표 파일(police.csv) 로드 중 오류 발생: " + e.getMessage());
        }
        return map;
    }

    private List<String> parseCsvLine(String line) {
        List<String> tokens = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;

        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                tokens.add(sb.toString().replace("\"", "").trim());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        tokens.add(sb.toString().replace("\"", "").trim());
        return tokens;
    }

    private int parseInteger(String input) {
        if (input == null || input.trim().isEmpty()) return 0;
        try {
            return (int) Double.parseDouble(input.trim().replace(",", ""));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private Double parseDouble(String input) {
        if (input == null || input.trim().isEmpty()) return 0.0;
        try {
            return Double.parseDouble(input.trim().replace(",", ""));
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private CrimeStat createCrimeStatObj(Region region, String type, int count, Double ratio, Double riskScore) {
        CrimeStat stat = new CrimeStat();
        stat.setRegion(region);
        stat.setCrimeType(type);
        stat.setCrimeCount(count);
        stat.setCrimeRatio(ratio);
        stat.setRiskScore(riskScore);
        stat.setStatYear(2026);
        return stat;
    }

    /**
     * CSV 파싱 데이터를 임시로 담아둘 이너 DTO 클래스
     */
    private static class PoliceStationRaw {
        String name;
        String type;
        Double longitude;
        Double latitude;

        public PoliceStationRaw(String name, String type, Double longitude, Double latitude) {
            this.name = name;
            this.type = type;
            this.longitude = longitude;
            this.latitude = latitude;
        }
    }
}