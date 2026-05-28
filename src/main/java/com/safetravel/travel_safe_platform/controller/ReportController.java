package com.safetravel.travel_safe_platform.controller;

import com.safetravel.travel_safe_platform.dto.ReportResponseDto;
import com.safetravel.travel_safe_platform.dto.ReportUpdateRequestDto;
import com.safetravel.travel_safe_platform.dto.ReportWriteRequestDto;
import com.safetravel.travel_safe_platform.entity.Report;
import com.safetravel.travel_safe_platform.service.ReportService;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(
            ReportService reportService
    ) {
        this.reportService = reportService;
    }

    // 신고글 작성
    @PostMapping
    public Report createReport(
            @RequestBody ReportWriteRequestDto dto
    ) {

        return reportService.saveReport(dto);
    }

    // 신고글 전체 조회 + 페이징
    @GetMapping
    public Page<ReportResponseDto> getReports(

            @RequestParam(defaultValue = "0")
            int page
    ) {

        return reportService.getAllReports(page);
    }

    // 신고글 수정
    @PutMapping("/{id}")
    public Report updateReport(
            @PathVariable Long id,
            @RequestBody ReportUpdateRequestDto dto
    ) {

        return reportService.updateReport(id, dto);
    }

    // 신고글 삭제
    @DeleteMapping("/{id}")
    public String deleteReport(
            @PathVariable Long id
    ) {

        reportService.deleteReport(id);

        return "삭제 완료";
    }
}