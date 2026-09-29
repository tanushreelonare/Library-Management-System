package com.example.library.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    // Login Page

    @GetMapping("/login")
    public String login() {

        return "login";

    }

    // Login Success

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication) {

        if (authentication != null) {

            boolean isStudent = authentication.getAuthorities()
                    .stream()
                    .anyMatch(authority ->
                            authority.getAuthority().equals("ROLE_STUDENT"));

            if (isStudent) {

                return "redirect:/student-dashboard";

            }

            boolean isLibrarian = authentication.getAuthorities()
                    .stream()
                    .anyMatch(authority ->
                            authority.getAuthority().equals("ROLE_LIBRARIAN"));

            if (isLibrarian) {

                return "redirect:/librarian-dashboard";

            }

        }

        return "redirect:/login";

    }

}