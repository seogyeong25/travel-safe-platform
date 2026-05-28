package com.safetravel.travel_safe_platform.controller;
import com.safetravel.travel_safe_platform.dto.ReportResponseDto;
import com.safetravel.travel_safe_platform.dto.ReportUpdateRequestDto;
import com.safetravel.travel_safe_platform.dto.ReportWriteRequestDto;
import com.safetravel.travel_safe_platform.entity.Report;
import com.safetravel.travel_safe_platform.service.ReportService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    // 신고글 작성
    @PostMapping
    public Report createReport(
            @RequestBody ReportWriteRequestDto dto) {

        return reportService.saveReport(dto);
    }

    // 신고글 전체 조회
    @GetMapping
    public List<ReportResponseDto> getReports() {

        return reportService.getAllReports();
    }

    // 신고글 수정
    @PutMapping("/{id}")
    public String updateReport(
            @PathVariable Long id,
            @ModelAttribute ReportUpdateRequestDto dto
    ) {

        reportService.updateReport(id, dto);

        return "redirect:/board/" + id;
    }
    // 신고글 삭제
    @DeleteMapping("/{id}")
    public void deleteReport(@PathVariable Long id) {

        reportService.deleteReport(id);
    }
}