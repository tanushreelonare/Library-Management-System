package com.example.library.controller;

import com.example.library.dto.LibrarianRegistrationDTO;
import com.example.library.service.LibrarianService;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/librarian")
public class LibrarianController {

    private final LibrarianService librarianService;

    public LibrarianController(
            LibrarianService librarianService) {

        this.librarianService = librarianService;

    }

    // Registration Page

    @GetMapping
    public String showRegistrationPage(
            Model model) {

        if (!model.containsAttribute("librarian")) {

            model.addAttribute(
                    "librarian",
                    new LibrarianRegistrationDTO()
            );

        }

        return "librarian";

    }

    // Register Librarian

    @PostMapping
    public String registerLibrarian(

            @Valid
            @ModelAttribute("librarian")
            LibrarianRegistrationDTO librarianDTO,

            BindingResult bindingResult,

            RedirectAttributes redirectAttributes,

            Model model) {

        // Validation Errors

        if (bindingResult.hasErrors()) {

            return "librarian";

        }

        try {

            librarianService.registerLibrarian(
                    librarianDTO
            );

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Librarian registered successfully."
            );

            return "redirect:/login";

        }

        catch (IllegalArgumentException ex) {

            model.addAttribute(
                    "errorMessage",
                    ex.getMessage()
            );

            return "librarian";

        }

        catch (IOException ex) {

            model.addAttribute(
                    "errorMessage",
                    "Unable to upload the librarian image."
            );

            return "librarian";

        }

        catch (Exception ex) {

            model.addAttribute(
                    "errorMessage",
                    "An unexpected error occurred. Please try again."
            );

            return "librarian";

        }

    }

}