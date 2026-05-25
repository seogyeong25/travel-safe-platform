package com.safetravel.travel_safe_platform.controller;

import com.safetravel.travel_safe_platform.entity.Report;
import com.safetravel.travel_safe_platform.service.ReportService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // 이 클래스는 API 요청 받는 곳
@RequestMapping("/reports") // "/reports" 주소 담당
public class ReportController {  // Report 관련 요청 처리 클래스

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    // 신고글 작성
    @PostMapping
    public Report createReport(@RequestBody Report report) {
        return reportService.saveReport(report);
    }

    // 신고글 전체 조회
    @GetMapping
    public List<Report> getReports() {
        return reportService.getAllReports();
    }

    // 신고글 수정
    @PutMapping("/{id}")  // 수정 요청 받기
    public Report updateReport(
            @PathVariable Long id, // URL 안의 번호 꺼내오기
            @RequestBody Report report) {  // 사용자가 보낸 JSON -> Report 객체로 자동 변환

        return reportService.updateReport(id, report);
    }

    // 신고글 삭제
    @DeleteMapping("/{id}")  // 삭제 요청 처리
    public void deleteReport(@PathVariable Long id) {

        reportService.deleteReport(id);
    }
}