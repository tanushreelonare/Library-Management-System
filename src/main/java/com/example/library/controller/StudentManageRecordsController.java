package com.example.library.controller;

import com.example.library.dto.IssuedBookDTO;
import com.example.library.service.IssueRequestService;
import com.example.library.service.StudentManageRecordsService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/student/manage-records")
public class StudentManageRecordsController {

    private final StudentManageRecordsService studentManageRecordsService;
    private final IssueRequestService issueRequestService;

    public StudentManageRecordsController(
            StudentManageRecordsService studentManageRecordsService,
            IssueRequestService issueRequestService) {

        this.studentManageRecordsService =
                studentManageRecordsService;

        this.issueRequestService =
                issueRequestService;
    }

    @GetMapping
    public String showManageRecords(
            @RequestParam(
                    name = "returned",
                    required = false,
                    defaultValue = "false"
            )
            boolean returned,

            @RequestParam(
                    name = "reissue",
                    required = false,
                    defaultValue = "false"
            )
            boolean reissue,

            HttpSession session,
            Model model) {

        String email = getAuthenticatedEmail();

        List<IssuedBookDTO> issuedBooks =
                studentManageRecordsService
                        .getStudentIssuedBooks(email);

        model.addAttribute(
                "issuedBooks",
                issuedBooks
        );

        model.addAttribute(
                "returned",
                returned
        );

        model.addAttribute(
                "reissue",
                reissue
        );

        return "student-manage-records";
    }

    @PostMapping("/return")
    public String returnBook(
            @RequestParam Long itemId,
            HttpSession session) {

        String email = getAuthenticatedEmail();

        studentManageRecordsService.returnBook(
                itemId,
                email
        );

        return "redirect:/student/manage-records?returned=true";
    }

    @PostMapping("/reissue")
    public String submitReissueRequest(
            @RequestParam Long itemId,
            @RequestParam Integer durationDays,
            RedirectAttributes redirectAttributes) {

        String email = getAuthenticatedEmail();

        try {

            issueRequestService.submitReissueRequest(
                    itemId,
                    durationDays,
                    email
            );

            redirectAttributes.addFlashAttribute(
                    "reissueSuccess",
                    "Re-issue request submitted successfully."
            );

        } catch (RuntimeException ex) {

            redirectAttributes.addFlashAttribute(
                    "reissueError",
                    ex.getMessage()
            );
        }

        return "redirect:/student/manage-records";
    }

    private String getAuthenticatedEmail() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication.getName() == null) {

            throw new RuntimeException(
                    "User is not authenticated"
            );
        }

        return authentication.getName();
    }
}