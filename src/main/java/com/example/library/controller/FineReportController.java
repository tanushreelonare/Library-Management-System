package com.example.library.controller;

import com.example.library.dto.FineReportDTO;
import com.example.library.service.FineReportService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/librarian")
public class FineReportController {

    private final FineReportService fineReportService;

    public FineReportController(
            FineReportService fineReportService) {

        this.fineReportService = fineReportService;
    }

    @GetMapping("/fine-report")
    public String showFineReport(
            @RequestParam(required = false) String search,
            @RequestParam(required = false, defaultValue = "ALL") String status,
            Model model) {

        List<FineReportDTO> fineReports =
                fineReportService.getFineReports(
                        search,
                        status
                );

        double totalFine = fineReports.stream()
                .mapToDouble(FineReportDTO::getFineAmount)
                .sum();

        long currentlyOverdue = fineReports.stream()
                .filter(report ->
                        "OVERDUE".equalsIgnoreCase(
                                report.getStatus()
                        ))
                .count();

        long returnedLate = fineReports.stream()
                .filter(report ->
                        "RETURNED LATE".equalsIgnoreCase(
                                report.getStatus()
                        ))
                .count();

        long totalOverdueDays = fineReports.stream()
                .mapToLong(FineReportDTO::getOverdueDays)
                .sum();

        model.addAttribute("fineReports", fineReports);
        model.addAttribute("search", search != null ? search : "");
        model.addAttribute("status", status);

        model.addAttribute("totalFine", totalFine);
        model.addAttribute("currentlyOverdue", currentlyOverdue);
        model.addAttribute("returnedLate", returnedLate);
        model.addAttribute("totalOverdueDays", totalOverdueDays);

        model.addAttribute("fineRate", 5);

        return "fine-report";
    }
}