package com.example.library.controller;

import com.example.library.dto.MyRequestDTO;
import com.example.library.service.IssueRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/student/requests")
public class StudentRequestController {

    private final IssueRequestService issueRequestService;

    public StudentRequestController(
            IssueRequestService issueRequestService) {

        this.issueRequestService =
                issueRequestService;
    }

    @GetMapping
    public String getMyRequests(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status,
            Authentication authentication,
            Model model) {

        String email =
                authentication.getName();

        List<MyRequestDTO> requests =
                issueRequestService.getStudentRequests(
                        email,
                        search,
                        status);

        model.addAttribute(
                "requests",
                requests);

        model.addAttribute(
                "search",
                search);

        model.addAttribute(
                "status",
                status == null || status.isBlank()
                        ? "ALL"
                        : status.toUpperCase());

        return "student-my-requests";
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> getRequestDetails(
            @PathVariable Long id,
            Authentication authentication) {

        try {

            String email =
                    authentication.getName();

            MyRequestDTO request =
                    issueRequestService
                            .getStudentRequestById(
                                    id,
                                    email);

            return ResponseEntity.ok(request);

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    @PostMapping("/{id}/cancel")
    public String cancelRequest(
            @PathVariable Long id,
            Authentication authentication) {

        try {

            String email =
                    authentication.getName();

            issueRequestService.cancelRequest(
                    id,
                    email);

        } catch (IllegalArgumentException ignored) {
            // The page will remain available if cancellation fails.
        }

        return "redirect:/student/requests";
    }
}