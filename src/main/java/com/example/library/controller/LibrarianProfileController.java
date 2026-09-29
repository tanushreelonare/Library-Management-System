package com.example.library.controller;

import com.example.library.dto.LibrarianProfileDTO;
import com.example.library.entity.User;
import com.example.library.service.LibrarianService;
import com.example.library.service.UserService;

import jakarta.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/librarian/profile")
public class LibrarianProfileController {

    private final LibrarianService librarianService;
    private final UserService userService;

    public LibrarianProfileController(
            LibrarianService librarianService,
            UserService userService) {

        this.librarianService = librarianService;
        this.userService = userService;
    }

    // Display Librarian Profile

    @GetMapping
    public String showProfile(
            Authentication authentication,
            Model model) {

        String email = authentication.getName();

        User user = userService.getUserByEmail(email)
                .orElse(null);

        if (user == null) {
            return "redirect:/login";
        }

        LibrarianProfileDTO librarianProfile =
                librarianService.getLibrarianProfile(
                        user.getId()
                );

        model.addAttribute(
                "librarianProfile",
                librarianProfile
        );

        return "librarian-profile";
    }

    // Update Librarian Profile

    @PostMapping
    public String updateProfile(
            @Valid @ModelAttribute("librarianProfile")
            LibrarianProfileDTO dto,
            BindingResult bindingResult,
            Authentication authentication,
            RedirectAttributes redirectAttributes,
            Model model) {

        String email = authentication.getName();

        User user = userService.getUserByEmail(email)
                .orElse(null);

        if (user == null) {
            return "redirect:/login";
        }

        // Validation errors

        if (bindingResult.hasErrors()) {

            LibrarianProfileDTO currentProfile =
                    librarianService.getLibrarianProfile(
                            user.getId()
                    );

            dto.setId(currentProfile.getId());
            dto.setEmployeeId(currentProfile.getEmployeeId());
            dto.setEmail(currentProfile.getEmail());
            dto.setActive(currentProfile.isActive());
            dto.setCreatedAt(currentProfile.getCreatedAt());
            dto.setImagePath(currentProfile.getImagePath());

            model.addAttribute(
                    "librarianProfile",
                    dto
            );

            return "librarian-profile";
        }

        try {

            librarianService.updateLibrarianProfile(
                    user.getId(),
                    dto
            );

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Profile updated successfully."
            );

            return "redirect:/librarian/profile";

        } catch (IllegalArgumentException e) {

            LibrarianProfileDTO currentProfile =
                    librarianService.getLibrarianProfile(
                            user.getId()
                    );

            dto.setId(currentProfile.getId());
            dto.setEmployeeId(currentProfile.getEmployeeId());
            dto.setEmail(currentProfile.getEmail());
            dto.setActive(currentProfile.isActive());
            dto.setCreatedAt(currentProfile.getCreatedAt());
            dto.setImagePath(currentProfile.getImagePath());

            model.addAttribute(
                    "librarianProfile",
                    dto
            );

            model.addAttribute(
                    "errorMessage",
                    e.getMessage()
            );

            return "librarian-profile";

        } catch (Exception e) {

            LibrarianProfileDTO currentProfile =
                    librarianService.getLibrarianProfile(
                            user.getId()
                    );

            dto.setId(currentProfile.getId());
            dto.setEmployeeId(currentProfile.getEmployeeId());
            dto.setEmail(currentProfile.getEmail());
            dto.setActive(currentProfile.isActive());
            dto.setCreatedAt(currentProfile.getCreatedAt());
            dto.setImagePath(currentProfile.getImagePath());

            model.addAttribute(
                    "librarianProfile",
                    dto
            );

            model.addAttribute(
                    "errorMessage",
                    "Unable to update profile. Please try again."
            );

            return "librarian-profile";
        }
    }
}