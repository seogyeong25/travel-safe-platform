package com.safetravel.travel_safe_platform.controller;

import com.safetravel.travel_safe_platform.dto.ReportResponseDto;
import com.safetravel.travel_safe_platform.dto.ReportUpdateRequestDto;
import com.safetravel.travel_safe_platform.dto.ReportWriteRequestDto;
import com.safetravel.travel_safe_platform.entity.Report;
import com.safetravel.travel_safe_platform.service.ReportService;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * 게시글·신고 REST API 컨트롤러.
 */
@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(
            ReportService reportService
    ) {
        this.reportService = reportService;
    }

    /**
     * 신고글을 작성합니다.
     */
    @PostMapping
    public Report createReport(
            @RequestBody ReportWriteRequestDto dto
    ) {

        return reportService.saveReport(dto);
    }

    /**
     * 신고글 목록을 페이징하여 조회합니다.
     */
    @GetMapping
    public Page<ReportResponseDto> getReports(

            @RequestParam(defaultValue = "0")
            int page
    ) {

        return reportService.getAllReports(page);
    }

    /**
     * 신고글을 수정합니다.
     */
    @PutMapping("/{id}")
    public Report updateReport(
            @PathVariable Long id,
            @RequestBody ReportUpdateRequestDto dto
    ) {

        return reportService.updateReport(id, dto);
    }

    /**
     * 신고글을 삭제합니다.
     */
    @DeleteMapping("/{id}")
    public String deleteReport(
            @PathVariable Long id
    ) {

        reportService.deleteReport(id);

        return "삭제 완료";
    }
}
