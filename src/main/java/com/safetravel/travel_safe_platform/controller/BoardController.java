package com.safetravel.travel_safe_platform.controller;

import com.safetravel.travel_safe_platform.dto.ReportResponseDto;
import com.safetravel.travel_safe_platform.dto.ReportUpdateRequestDto;
import com.safetravel.travel_safe_platform.dto.ReportWriteRequestDto;
import com.safetravel.travel_safe_platform.repository.RegionRepository;
import com.safetravel.travel_safe_platform.service.ReportService;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 게시판(안전 신고 글) MVC 컨트롤러.
 */
@Controller
public class BoardController {

    private final ReportService reportService;
    private final RegionRepository regionRepository;

    public BoardController(
            ReportService reportService,
            RegionRepository regionRepository
    ) {
        this.reportService = reportService;
        this.regionRepository = regionRepository;
    }

    private void addRegionOptions(Model model) {
        model.addAttribute("regions", regionRepository.findAll());
        model.addAttribute("sidoList", regionRepository.findDistinctSido());
    }

    /** 글 작성 화면 */
    @GetMapping("/board/write")
    public String writePage(Model model) {
        addRegionOptions(model);
        return "board-write";
    }

    /** 글 작성 처리 */
    @PostMapping("/board/write")
    public String writeReport(
            @ModelAttribute ReportWriteRequestDto dto
    ) {
        reportService.saveReport(dto);
        return "redirect:/board";
    }

    /** 글 상세 화면 (조회수 증가) */
    @GetMapping("/board/{id}")
    public String boardDetailPage(
            @PathVariable Long id,
            Model model
    ) {
        reportService.increaseViews(id);

        ReportResponseDto report =
                reportService.getReport(id);

        model.addAttribute("report", report);
        model.addAttribute("canEdit", reportService.canCurrentUserModify(id));

        return "board-detail";
    }

    /** 글 수정 화면 */
    @GetMapping("/board/edit/{id}")
    public String editPage(
            @PathVariable Long id,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (!reportService.canCurrentUserModify(id)) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "본인이 작성한 글만 수정할 수 있습니다."
            );
            return "redirect:/board/" + id;
        }

        ReportResponseDto report =
                reportService.getReport(id);

        model.addAttribute("report", report);
        addRegionOptions(model);

        return "board-edit";
    }

    /** 글 수정 처리 */
    @PostMapping("/board/edit/{id}")
    public String updateBoard(
            @PathVariable Long id,
            @ModelAttribute ReportUpdateRequestDto dto
    ) {
        reportService.updateReport(id, dto);
        return "redirect:/board/" + id;
    }

    /** 글 삭제 */
    @DeleteMapping("/board/{id}")
    public String deleteReport(
            @PathVariable Long id
    ) {
        reportService.deleteReport(id);
        return "redirect:/board";
    }

    /** 게시판 목록 (검색·필터·페이징) */
    @GetMapping("/board")
    public String boardPage(
            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(required = false)
            String keyword,

            @RequestParam(required = false)
            String category,

            @RequestParam(required = false)
            String sido,

            @RequestParam(required = false)
            String sigungu,

            Model model
    ) {
        Page<ReportResponseDto> reports;

        if (sido != null && !sido.isEmpty()) {
            reports = reportService.searchReportsByRegion(
                    sido,
                    sigungu,
                    page
            );
        } else if (keyword != null && !keyword.isEmpty()) {
            reports = reportService.searchReports(
                    keyword,
                    page
            );
        } else if (category != null && !category.isEmpty()) {
            reports = reportService.getReportsByCategory(
                    category,
                    page
            );
        } else {
            reports = reportService.getAllReports(page);
        }

        model.addAttribute("reports", reports);
        model.addAttribute("currentPage", page);
        model.addAttribute(
                "totalPages",
                reports.getTotalPages()
        );
        addRegionOptions(model);
        model.addAttribute("selectedSido", sido);
        model.addAttribute("selectedSigungu", sigungu);

        return "board";
    }
}
