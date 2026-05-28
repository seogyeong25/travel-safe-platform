package com.safetravel.travel_safe_platform.controller;

import com.safetravel.travel_safe_platform.entity.Report;
import com.safetravel.travel_safe_platform.service.ReportService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class BoardController {

    private final ReportService reportService;

    public BoardController(ReportService reportService) {
        this.reportService = reportService;
    }

    // 게시판 목록
    @GetMapping("/board")
    public String boardPage(Model model) {

        // DB에서 게시글 가져오기
        List<Report> reports = reportService.getAllReports();

        // HTML로 전달
        model.addAttribute("reports", reports);

        return "board";
    }

    // 글쓰기 페이지
    @GetMapping("/board/write")
    public String writePage() {
        return "board-write";
    }
}