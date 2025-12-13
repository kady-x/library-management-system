package com.aiu.library.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aiu.library.service.ReportService;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/overdue")
    public List<com.aiu.library.model.BorrowRecord> getOverdueBooks(
            @RequestParam(required = false) Integer limit) {
        if (limit != null) {
            return reportService.getOverdueBooks(limit);
        }
        return reportService.getOverdueBooks();
    }

    @GetMapping("/overdue-filtered")
    public List<com.aiu.library.model.BorrowRecord> getOverdueBooksFiltered(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) Integer limit) {
        if (startDate != null && endDate != null) {
            if (limit != null) {
                return reportService.getOverdueBooks(startDate, endDate, limit);
            }
            return reportService.getOverdueBooks(startDate, endDate);
        }
        return getOverdueBooks(limit);
    }

    @GetMapping("/most-borrowed")
    public List<Map<String, Object>> getMostBorrowedBooks(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) Integer limit) {
        if (startDate != null && endDate != null) {
            if (limit != null) {
                return reportService.getMostBorrowedBooks(startDate, endDate, limit);
            }
            return reportService.getMostBorrowedBooks(startDate, endDate);
        }
        if (limit != null) {
            return reportService.getMostBorrowedBooks(limit);
        }
        return reportService.getMostBorrowedBooks();
    }

    @GetMapping("/member-activity")
    public List<Map<String, Object>> getMemberActivity(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) Integer limit) {
        if (startDate != null && endDate != null) {
            if (limit != null) {
                return reportService.getMemberActivity(startDate, endDate, limit);
            }
            return reportService.getMemberActivity(startDate, endDate);
        }
        if (limit != null) {
            return reportService.getMemberActivity(limit);
        }
        return reportService.getMemberActivity();
    }
}
