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
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import com.safetravel.travel_safe_platform.dto.ReportWriteRequestDto;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 게시글·안전 신고 CRUD 및 검색 비즈니스 로직.
 */
@Service
public class ReportService {

    private final ReportRepository reportRepository;



    private final RegionRepository regionRepository;

    private final CurrentUserService currentUserService;

    public ReportService(
            ReportRepository reportRepository,
            RegionRepository regionRepository,
            CurrentUserService currentUserService
    ) {
        this.reportRepository = reportRepository;
        this.regionRepository = regionRepository;
        this.currentUserService = currentUserService;
    }

    /**
     * 신규 게시글을 저장합니다.
     *
     * @param dto 작성 요청 DTO
     * @return 저장된 게시글 엔티티
     */
    public Report saveReport(ReportWriteRequestDto dto) {

        User user = currentUserService.getCurrentUser();

        if (dto.getRegionId() == null) {
            throw new RuntimeException("지역을 선택해주세요.");
        }

        Region region = regionRepository.findById(dto.getRegionId())
                .orElseThrow(() ->
                        new RuntimeException("존재하지 않는 지역입니다."));

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

    /**
     * 전체 게시글을 페이징하여 DTO로 반환합니다.
     *
     * @param page 페이지 번호 (0부터)
     * @return 게시글 DTO 페이지
     */
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

    /**
     * 게시글을 수정합니다. 작성자만 수정 가능합니다.
     *
     * @param id  게시글 ID
     * @param dto 수정 요청 DTO
     * @return 수정된 게시글 엔티티
     */
    public Report updateReport(
            Long id,
            ReportUpdateRequestDto dto
    ) {

        Report report = reportRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("글 없음"));

        assertCurrentUserIsAuthor(report);

        report.setTitle(dto.getTitle());

        report.setContent(dto.getContent());

        report.setDangerLevel(dto.getDangerLevel());

        report.setCategory(dto.getCategory());

        if (dto.getRegionId() != null) {
            Region region = regionRepository.findById(dto.getRegionId())
                    .orElseThrow(() ->
                            new RuntimeException("존재하지 않는 지역입니다."));
            report.setRegion(region);
        }

        return reportRepository.save(report);
    }

    /**
     * 현재 로그인 사용자가 해당 게시글 작성자인지 여부를 반환합니다.
     *
     * @param reportId 게시글 ID
     * @return 작성자이면 true
     */
    public boolean canCurrentUserModify(Long reportId) {
        User current = currentUserService.getCurrentUserOptional().orElse(null);
        if (current == null) {
            return false;
        }
        Report report = reportRepository.findById(reportId).orElse(null);
        if (report == null || report.getUser() == null) {
            return false;
        }
        return Objects.equals(report.getUser().getId(), current.getId());
    }

    private void assertCurrentUserIsAuthor(Report report) {
        User current = currentUserService.getCurrentUser();
        if (report.getUser() == null
                || !Objects.equals(report.getUser().getId(), current.getId())) {
            throw new AccessDeniedException("본인이 작성한 글만 수정·삭제할 수 있습니다.");
        }
    }

    /**
     * 게시글을 삭제합니다. 작성자만 삭제 가능합니다.
     *
     * @param id 게시글 ID
     */
    public void deleteReport(Long id) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글 없음"));
        assertCurrentUserIsAuthor(report);
        reportRepository.deleteById(id);
    }

    /**
     * 게시글 상세를 DTO로 반환합니다.
     *
     * @param id 게시글 ID
     * @return 상세 DTO
     */
    public ReportResponseDto getReport(Long id) {

        Report report = reportRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("게시글 없음"));

        return new ReportResponseDto(report);
    }

    /**
     * 제목 키워드로 게시글을 검색합니다.
     *
     * @param keyword 검색어
     * @param page    페이지 번호
     * @return 검색 결과 DTO 페이지
     */
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

    /**
     * 카테고리별 게시글을 조회합니다.
     *
     * @param category 카테고리
     * @param page     페이지 번호
     * @return DTO 페이지
     */
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

    /**
     * 시·도·시군구 조건으로 게시글을 검색합니다.
     *
     * @param sido    시·도
     * @param sigungu 시·군·구 (선택)
     * @param page    페이지 번호
     * @return DTO 페이지
     */
    public Page<ReportResponseDto> searchReportsByRegion(
            String sido,
            String sigungu,
            int page
    ) {
        Pageable pageable =
                PageRequest.of(
                        page,
                        10,
                        Sort.by(Sort.Direction.DESC, "createdAt")
                );

        Page<Report> result;

        if (sigungu != null && !sigungu.isBlank()) {
            result = reportRepository
                    .findByRegion_SidoAndRegion_SigunguContaining(
                            sido,
                            sigungu,
                            pageable
                    );
        } else {
            result = reportRepository.findByRegion_Sido(sido, pageable);
        }

        return result.map(ReportResponseDto::new);
    }

    /**
     * 게시글 조회수를 1 증가시킵니다.
     *
     * @param id 게시글 ID
     */
    public void increaseViews(Long id) {

        Report report = reportRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("게시글 없음"));

        report.setViews(report.getViews() + 1);

        reportRepository.save(report);
    }
}
