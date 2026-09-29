package com.example.library.controller;

import com.example.library.dto.StudentRegistrationDTO;
import com.example.library.service.StudentService;

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
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {

        this.studentService = studentService;

    }

    // Registration Page

    @GetMapping
    public String showRegistrationPage(Model model) {

        if (!model.containsAttribute("student")) {

            model.addAttribute(
                    "student",
                    new StudentRegistrationDTO()
            );

        }

        return "student";

    }

    // Register Student

    @PostMapping
    public String registerStudent(

            @Valid
            @ModelAttribute("student")
            StudentRegistrationDTO studentDTO,

            BindingResult bindingResult,

            RedirectAttributes redirectAttributes,

            Model model) {

        // Validation Errors

        if (bindingResult.hasErrors()) {

            return "student";

        }

        try {

            studentService.registerStudent(studentDTO);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Student registered successfully."
            );

            return "redirect:/login";

        }

        catch (IllegalArgumentException ex) {

            model.addAttribute(
                    "errorMessage",
                    ex.getMessage()
            );

            return "student";

        }

        catch (IOException ex) {

            model.addAttribute(
                    "errorMessage",
                    "Unable to upload the student image."
            );

            return "student";

        }

        catch (Exception ex) {

            model.addAttribute(
                    "errorMessage",
                    "An unexpected error occurred. Please try again."
            );

            return "student";

        }

    }

}