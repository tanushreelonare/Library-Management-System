package com.example.library.controller;

import com.example.library.dto.DonationRequestDTO;
import com.example.library.entity.Student;
import com.example.library.entity.User;
import com.example.library.repository.StudentRepository;
import com.example.library.repository.UserRepository;
import com.example.library.service.DonationRequestService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentDonationController {

    private final DonationRequestService donationRequestService;
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;

    public StudentDonationController(
            DonationRequestService donationRequestService,
            StudentRepository studentRepository,
            UserRepository userRepository) {

        this.donationRequestService = donationRequestService;
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
    }


    // Display student donation page

    @GetMapping("/donations")
    public String showDonationsPage(
            Model model,
            Principal principal) {

        Student student = getCurrentStudent(principal);

        System.out.println(
                "DONATION DEBUG -> principal="
                        + principal.getName()
                        + ", studentId="
                        + student.getId()
        );

        List<DonationRequestDTO> requests =
                donationRequestService
                        .getStudentDonationRequests(
                                student.getId()
                        );

        model.addAttribute(
                "donationRequest",
                new DonationRequestDTO()
        );

        model.addAttribute(
                "donationRequests",
                requests
        );

        model.addAttribute(
                "studentId",
                student.getId()
        );

        return "student-donations";
    }


    // Submit student donation request

    @PostMapping("/donations")
    public String submitDonationRequest(
            DonationRequestDTO donationRequest,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        Student student = getCurrentStudent(principal);

        try {

            donationRequestService.submitDonationRequest(
                    student.getId(),
                    donationRequest
            );

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Donation request submitted successfully."
            );

        } catch (IllegalArgumentException |
                 IllegalStateException exception) {

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    exception.getMessage()
            );
        }

        return "redirect:/student/donations";
    }


    // Get currently logged-in student

    private Student getCurrentStudent(
            Principal principal) {

        if (principal == null ||
                principal.getName() == null ||
                principal.getName().isBlank()) {

            throw new IllegalStateException(
                    "No authenticated student found."
            );
        }

        String email = principal.getName();

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "User account not found."
                        )
                );

        return studentRepository
                .findByUserId(user.getId())
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Student profile not found."
                        )
                );
    }
}