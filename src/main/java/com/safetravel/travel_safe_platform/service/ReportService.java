package com.safetravel.travel_safe_platform.service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.safetravel.travel_safe_platform.dto.ReportUpdateRequestDto;
import com.safetravel.travel_safe_platform.dto.ReportResponseDto;
import com.safetravel.travel_safe_platform.entity.Region;
import com.safetravel.travel_safe_platform.entity.Report;
import com.safetravel.travel_safe_platform.entity.User;
import com.safetravel.travel_safe_platform.repository.RegionRepository;
import com.safetravel.travel_safe_platform.repository.ReportRepository;
import com.safetravel.travel_safe_platform.repository.UserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.safetravel.travel_safe_platform.dto.ReportWriteRequestDto;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportService {

    private final ReportRepository reportRepository;



    private final UserRepository userRepository;

    private final RegionRepository regionRepository;

    // 생성자
    public ReportService(
            ReportRepository reportRepository,
            UserRepository userRepository,
            RegionRepository regionRepository
    ) {
        this.reportRepository = reportRepository;

        this.userRepository = userRepository;

        this.regionRepository = regionRepository;
    }

    // 신고글 저장
    public Report saveReport(ReportWriteRequestDto dto) {

        // 임시 사용자
        User user = userRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("유저 없음"));

        // 임시 지역
        Region region = regionRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("지역 없음"));

        // Report 객체 생성
        Report report = Report.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .dangerLevel(dto.getDangerLevel())
                .category(dto.getCategory())
                .createdAt(LocalDateTime.now())
                .user(user)
                .region(region)
                .build();

        return reportRepository.save(report);
    }
    // 신고글 전체 조회
// 신고글 전체 조회
    public Page<ReportResponseDto> getAllReports(int page) {

        Pageable pageable =
                PageRequest.of(
                        page,
                        10,
                        Sort.by(Sort.Direction.DESC, "createdAt")
                );

        return reportRepository.findAll(pageable)
                .map(ReportResponseDto::new);
    }

    // 신고글 수정
    public Report updateReport(
            Long id,
            ReportUpdateRequestDto dto
    ) {

        // 기존 글 찾기
        Report report = reportRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("글 없음"));

        // 값 수정
        report.setTitle(dto.getTitle());

        report.setContent(dto.getContent());

        report.setDangerLevel(dto.getDangerLevel());

        report.setCategory(dto.getCategory());

        // 저장
        return reportRepository.save(report);
    }

    // 신고글 삭제
    public void deleteReport(Long id) {

        reportRepository.deleteById(id);
    }

    // 게시글 상세 조회
    public ReportResponseDto getReport(Long id) {

        Report report = reportRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("게시글 없음"));

        return new ReportResponseDto(report);
    }

    // 게시글 검색
    public Page<ReportResponseDto> searchReports(
            String keyword,
            int page
    ) {

        Pageable pageable =
                PageRequest.of(
                        page,
                        10,
                        Sort.by(Sort.Direction.DESC, "createdAt")
                );

        return reportRepository
                .findByTitleContaining(keyword, pageable)
                .map(ReportResponseDto::new);
    }

    // 카테고리별 조회
    public Page<ReportResponseDto> getReportsByCategory(
            String category,
            int page
    ) {

        Pageable pageable =
                PageRequest.of(
                        page,
                        10,
                        Sort.by(Sort.Direction.DESC, "createdAt")
                );

        return reportRepository
                .findByCategory(category, pageable)
                .map(ReportResponseDto::new);
    }

    // 조회수 증가
    public void increaseViews(Long id) {

        Report report = reportRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("게시글 없음"));

        report.setViews(report.getViews() + 1);

        reportRepository.save(report);
    }
}