package com.safetravel.travel_safe_platform.config;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * MVC 전역 예외 처리.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 접근 거부 시 게시판 목록으로 리다이렉트하며 오류 메시지를 전달합니다.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDenied(
            AccessDeniedException ex,
            RedirectAttributes redirectAttributes
    ) {
        String message = ex.getMessage();
        if (message == null || message.isBlank()) {
            message = "권한이 없습니다.";
        }
        redirectAttributes.addFlashAttribute("errorMessage", message);
        return "redirect:/board";
    }
}
