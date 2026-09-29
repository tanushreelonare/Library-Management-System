package com.example.library.controller;

import com.example.library.security.CustomUserDetails;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    // STUDENT DASHBOARD

    @GetMapping("/student-dashboard")
    public String showStudentDashboard(

            @AuthenticationPrincipal
            CustomUserDetails userDetails,

            Model model) {

        String studentName = userDetails
                .getUser()
                .getStudent()
                .getStudentName();

        model.addAttribute(
                "studentName",
                studentName
        );

        return "student-dashboard";

    }

    // LIBRARIAN DASHBOARD

    @GetMapping("/librarian-dashboard")
    public String showLibrarianDashboard(

            @AuthenticationPrincipal
            CustomUserDetails userDetails,

            Model model) {

        String librarianName = userDetails
                .getUser()
                .getLibrarian()
                .getLibrarianName();

        model.addAttribute(
                "librarianName",
                librarianName
        );

        return "librarian-dashboard";

    }

}