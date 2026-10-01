package com.example.library.controller;

import com.example.library.dto.DonationRequestDTO;
import com.example.library.service.DonationRequestService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/librarian")
public class LibrarianDonationController {

    private final DonationRequestService donationRequestService;

    public LibrarianDonationController(
            DonationRequestService donationRequestService) {

        this.donationRequestService = donationRequestService;
    }

    // Display donation management page

    @GetMapping("/donations")
    public String showDonationsPage(
            @RequestParam(required = false) String search,
            Model model) {

        List<DonationRequestDTO> donationRequests;

        if (search != null && !search.trim().isEmpty()) {

            donationRequests =
                    donationRequestService.searchDonationRequests(
                            search.trim()
                    );

        } else {

            donationRequests =
                    donationRequestService.getAllDonationRequests();
        }

        model.addAttribute(
                "donationRequests",
                donationRequests
        );

        model.addAttribute(
                "search",
                search
        );

        return "librarian-donations";
    }

    // Get donation details for preview

    @GetMapping("/donations/{id}")
    @org.springframework.web.bind.annotation.ResponseBody
    public DonationRequestDTO getDonationDetails(
            @PathVariable Long id) {

        return donationRequestService
                .getDonationRequestById(id);
    }

    // Approve donation request

    @PostMapping("/donations/{id}/approve")
    public String approveDonation(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        try {

            donationRequestService
                    .approveDonationRequest(id);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Donation request approved successfully."
            );

        } catch (IllegalArgumentException
                 | IllegalStateException exception) {

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    exception.getMessage()
            );
        }

        return "redirect:/librarian/donations";
    }

    // Reject donation request

    @PostMapping("/donations/{id}/reject")
    public String rejectDonation(
            @PathVariable Long id,
            @RequestParam String rejectionReason,
            RedirectAttributes redirectAttributes) {

        try {

            donationRequestService
                    .rejectDonationRequest(
                            id,
                            rejectionReason
                    );

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Donation request rejected successfully."
            );

        } catch (IllegalArgumentException
                 | IllegalStateException exception) {

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    exception.getMessage()
            );
        }

        return "redirect:/librarian/donations";
    }
}