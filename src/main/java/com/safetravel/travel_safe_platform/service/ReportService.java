package com.safetravel.travel_safe_platform.service;

import com.safetravel.travel_safe_platform.entity.Region;
import com.safetravel.travel_safe_platform.entity.Report;
import com.safetravel.travel_safe_platform.entity.User;
import com.safetravel.travel_safe_platform.repository.RegionRepository;
import com.safetravel.travel_safe_platform.repository.ReportRepository;
import com.safetravel.travel_safe_platform.repository.UserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportService {

    private final ReportRepository reportRepository; // Repository를 한 번 연결하면 계속 그것만 사용한다는 의미
    private final UserRepository userRepository;
    private final RegionRepository regionRepository;

    // 생성자 - Repository 가져와 주세요라는 의미
    public ReportService(
            ReportRepository reportRepository,
            UserRepository userRepository,
            RegionRepository regionRepository
    ) {
        this.reportRepository = reportRepository;  // 밖에서 가져온 Repository를 내 안에 저장
        this.userRepository = userRepository;
        this.regionRepository = regionRepository;
    }


    // 신고글 저장
    public Report saveReport(Report report) {

        // 임시 사용자
        User user = userRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("유저 없음"));

        // 임시 지역
        Region region = regionRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("지역 없음"));

        report.setUser(user);  // report <-> user 실제 연결
        report.setRegion(region);

        report.setCreatedAt(LocalDateTime.now());

        return reportRepository.save(report); // save()가 저장된 Report 객체를 반환함.
    }


    // 신고글 전체 조회
    public List<Report> getAllReports() {
        return reportRepository.findAll(
                Sort.by(Sort.Direction.DESC, "createdAt")  // 최신순 정렬
        );
    }


    // 신고글 수정
    public Report updateReport(Long id, Report updatedReport) {

        // 기존 글 찾기
        Report report = reportRepository.findById(id)  // Optional<Report> 반환함
                .orElseThrow(() -> new RuntimeException("글 없음"));

        // 값 수정
        report.setTitle(updatedReport.getTitle());
        report.setContent(updatedReport.getContent());
        report.setDangerLevel(updatedReport.getDangerLevel());

        // 다시 저장
        return reportRepository.save(report);
    }


    // 신고글 삭제
    public void deleteReport(Long id) {

        reportRepository.deleteById(id);
    }
}