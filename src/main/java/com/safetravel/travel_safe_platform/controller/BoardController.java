package com.safetravel.travel_safe_platform.controller;

import org.springframework.data.domain.Page;
import com.safetravel.travel_safe_platform.dto.ReportResponseDto;
import com.safetravel.travel_safe_platform.dto.ReportUpdateRequestDto;
import com.safetravel.travel_safe_platform.dto.ReportWriteRequestDto;
import com.safetravel.travel_safe_platform.service.ReportService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class BoardController {

    private final ReportService reportService;

    public BoardController(ReportService reportService) {
        this.reportService = reportService;
    }



    // 글쓰기 페이지
    @GetMapping("/board/write")
    public String writePage() {
        return "board-write";
    }

    // 글 저장
    @PostMapping("/board/write")
    public String writeReport(
            @ModelAttribute ReportWriteRequestDto dto
    ) {

        reportService.saveReport(dto);

        return "redirect:/board";
    }

    @GetMapping("/board/{id}")
    public String boardDetailPage(
            @PathVariable Long id,
            Model model
    ) {

        // 조회수 증가
        reportService.increaseViews(id);

        ReportResponseDto report =
                reportService.getReport(id);

        model.addAttribute("report", report);

        return "board-detail";
    }


//     글 수정
    @GetMapping("/board/edit/{id}")
    public String editPage(
            @PathVariable Long id,
            Model model
    ) {

        ReportResponseDto report =
                reportService.getReport(id);

        model.addAttribute("report", report);

        return "board-edit";
    }

    @PostMapping("/board/edit/{id}")
    public String updateBoard(
            @PathVariable Long id,
            @ModelAttribute ReportUpdateRequestDto dto
    ) {

        reportService.updateReport(id, dto);

        return "redirect:/board/" + id;
    }


// 글 삭제
@DeleteMapping("/board/{id}")
public String deleteReport(
        @PathVariable Long id
) {

    reportService.deleteReport(id);

    return "redirect:/board";
}

    @GetMapping("/board")
    public String boardPage(
            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(required = false)
            String keyword,

            @RequestParam(required = false)
            String category,

            Model model
    ) {

        Page<ReportResponseDto> reports;

        // 검색
        if (keyword != null && !keyword.isEmpty()) {

            reports = reportService.searchReports(
                    keyword,
                    page
            );
        }

// 카테고리
        else if (category != null && !category.isEmpty()) {

            reports = reportService.getReportsByCategory(
                    category,
                    page
            );
        }

        // 전체
        else {

            reports = reportService.getAllReports(page);
        }

        model.addAttribute("reports", reports);

        model.addAttribute("currentPage", page);

        model.addAttribute(
                "totalPages",
                reports.getTotalPages()
        );

        return "board";
    }
}