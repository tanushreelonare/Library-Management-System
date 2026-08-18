package com.example.library.controller;

import com.example.library.service.UserService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PasswordController {

    private final UserService userService;

    public PasswordController(
            UserService userService) {

        this.userService = userService;

    }

    // Forgot Password Page

    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {

        return "forgot-password";

    }

    // Forgot Password

    @PostMapping("/forgot-password")
    public String forgotPassword(

            @RequestParam("email")
            String email,

            RedirectAttributes redirectAttributes,

            Model model) {

        try {

            if (userService.getUserByEmail(email).isEmpty()) {

                model.addAttribute(
                        "errorMessage",
                        "No account found with this email address."
                );

                return "forgot-password";

            }

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Email verified. Please update your password."
            );

            return "redirect:/update-password?email=" + email;

        }

        catch (Exception ex) {

            model.addAttribute(
                    "errorMessage",
                    "Unable to process your request."
            );

            return "forgot-password";

        }

    }

    // Update Password Page

    @GetMapping("/update-password")
    public String updatePasswordPage(

            @RequestParam(required = false)
            String email,

            Model model) {

        model.addAttribute(
                "email",
                email
        );

        return "update-password";

    }

    // Update Password

    @PostMapping("/update-password")
    public String updatePassword(

            @RequestParam
            String email,

            @RequestParam
            String password,

            @RequestParam
            String confirmPassword,

            RedirectAttributes redirectAttributes,

            Model model) {

        try {

            if (!password.equals(confirmPassword)) {

                model.addAttribute(
                        "email",
                        email
                );

                model.addAttribute(
                        "errorMessage",
                        "Passwords do not match."
                );

                return "update-password";

            }

            var user =
                    userService.getUserByEmail(email);

            if (user.isEmpty()) {

                model.addAttribute(
                        "errorMessage",
                        "User not found."
                );

                return "update-password";

            }

            userService.updatePassword(
                    user.get().getId(),
                    password
            );

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Password updated successfully. Please login."
            );

            return "redirect:/login";

        }

        catch (Exception ex) {

            model.addAttribute(
                    "email",
                    email
            );

            model.addAttribute(
                    "errorMessage",
                    "Unable to update password."
            );

            return "update-password";

        }

    }

}