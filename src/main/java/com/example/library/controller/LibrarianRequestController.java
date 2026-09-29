package com.example.library.controller;

import com.example.library.dto.MyRequestDTO;
import com.example.library.service.IssueRequestService;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/librarian/requests")
public class LibrarianRequestController {

    private final IssueRequestService issueRequestService;

    public LibrarianRequestController(
            IssueRequestService issueRequestService) {

        this.issueRequestService =
                issueRequestService;
    }

    // Request list

    @GetMapping
    public String getRequests(
            @RequestParam(required = false)
            String search,

            @RequestParam(
                    required = false,
                    defaultValue = "PENDING"
            )
            String status,

            Model model) {

        List<MyRequestDTO> requests =
                issueRequestService
                        .getAllLibrarianRequests(
                                search,
                                status
                        );

        model.addAttribute(
                "requests",
                requests
        );

        model.addAttribute(
                "search",
                search
        );

        model.addAttribute(
                "status",
                status
        );

        return "librarian-requests";
    }

    // Request details

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<MyRequestDTO>
    getRequestById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                issueRequestService
                        .getLibrarianRequestById(id)
        );
    }

    // Approve all pending books

    @PostMapping("/{id}/approve")
    public String approveRequest(
            @PathVariable Long id) {

        issueRequestService.approveRequest(id);

        return "redirect:/librarian/requests?approved=true";
    }

    // Reject all pending books

    @PostMapping("/{id}/reject")
    public String rejectRequest(
            @PathVariable Long id,

            @RequestParam(
                    name = "rejectionReason",
                    required = false
            )
            String rejectionReason) {

        issueRequestService.rejectRequest(
                id,
                rejectionReason
        );

        return "redirect:/librarian/requests?rejected=true";
    }

    // Approve selected books

    @PostMapping("/{id}/approve-selected")
    public String approveSelectedBooks(
            @PathVariable Long id,

            @RequestParam(
                    name = "itemIds"
            )
            List<Long> itemIds) {

        issueRequestService
                .approveRequestItems(
                        id,
                        itemIds
                );

        return "redirect:/librarian/requests?approved=true";
    }

    // Reject selected books

    @PostMapping("/{id}/reject-selected")
    public String rejectSelectedBooks(
            @PathVariable Long id,

            @RequestParam(
                    name = "itemIds"
            )
            List<Long> itemIds,

            @RequestParam(
                    name = "rejectionReason",
                    required = false
            )
            String rejectionReason) {

        issueRequestService
                .rejectRequestItems(
                        id,
                        itemIds,
                        rejectionReason
                );

        return "redirect:/librarian/requests?rejected=true";
    }

    // Approve re-issue request

    @PostMapping("/{id}/approve-reissue")
    public String approveReissueRequest(
            @PathVariable Long id) {

        issueRequestService
                .approveReissueRequest(id);

        return "redirect:/librarian/requests?reissueApproved=true";
    }

    // Reject re-issue request

    @PostMapping("/{id}/reject-reissue")
    public String rejectReissueRequest(
            @PathVariable Long id,

            @RequestParam(
                    name = "rejectionReason",
                    required = false
            )
            String rejectionReason) {

        issueRequestService
                .rejectReissueRequest(
                        id,
                        rejectionReason
                );

        return "redirect:/librarian/requests?reissueRejected=true";
    }
}