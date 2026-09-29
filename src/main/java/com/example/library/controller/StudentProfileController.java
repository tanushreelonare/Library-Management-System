package com.example.library.controller;

import com.example.library.dto.StudentProfileDTO;
import com.example.library.entity.User;
import com.example.library.service.StudentService;
import com.example.library.service.UserService;

import jakarta.validation.Valid;

import org.springframework.security.core.Authentication;

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
@RequestMapping("/student/profile")
public class StudentProfileController {

    private final StudentService studentService;

    private final UserService userService;

    public StudentProfileController(
            StudentService studentService,
            UserService userService) {

        this.studentService = studentService;
        this.userService = userService;

    }

    // STUDENT PROFILE PAGE

    @GetMapping
    public String showStudentProfile(
            Authentication authentication,
            Model model) {

        String email =
                authentication.getName();

        User user =
                userService.getUserByEmail(email)

                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "User not found."
                                )
                        );

        StudentProfileDTO studentProfile =
                studentService.getStudentProfile(
                        user.getId()
                );

        model.addAttribute(
                "studentProfile",
                studentProfile
        );

        return "student-profile";

    }

    // UPDATE STUDENT PROFILE

    @PostMapping
    public String updateStudentProfile(
            @Valid
            @ModelAttribute("studentProfile")
            StudentProfileDTO studentProfileDTO,
            BindingResult bindingResult,
            Authentication authentication,
            Model model,
            RedirectAttributes redirectAttributes) {

        String email =
                authentication.getName();

        try {

            User user =
                    userService.getUserByEmail(email)

                            .orElseThrow(() ->
                                    new IllegalArgumentException(
                                            "User not found."
                                    )
                            );

            if (bindingResult.hasErrors()) {

                loadAccountInformation(
                        studentProfileDTO,
                        user
                );

                model.addAttribute(
                        "studentProfile",
                        studentProfileDTO
                );

                return "student-profile";

            }

            studentService.updateStudentProfile(
                    user.getId(),
                    studentProfileDTO
            );

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Profile updated successfully."
            );

            return "redirect:/student/profile";

        }

        catch (IllegalArgumentException ex) {

            loadProfileForError(
                    authentication,
                    studentProfileDTO,
                    model
            );

            model.addAttribute(
                    "errorMessage",
                    ex.getMessage()
            );

            return "student-profile";

        }

        catch (IOException ex) {

            loadProfileForError(
                    authentication,
                    studentProfileDTO,
                    model
            );

            model.addAttribute(
                    "errorMessage",
                    "Unable to upload the profile image."
            );

            return "student-profile";

        }

        catch (Exception ex) {

            loadProfileForError(
                    authentication,
                    studentProfileDTO,
                    model
            );

            model.addAttribute(
                    "errorMessage",
                    "Unable to update the student profile."
            );

            return "student-profile";

        }

    }

    // LOAD ACCOUNT INFORMATION

    private void loadAccountInformation(
            StudentProfileDTO studentProfileDTO,
            User user) {

        studentProfileDTO.setEmail(
                user.getEmail()
        );

        studentProfileDTO.setEnabled(
                user.isEnabled()
        );

        studentProfileDTO.setCreatedAt(
                user.getCreatedAt()
        );

    }

    // LOAD PROFILE AFTER ERROR

    private void loadProfileForError(
            Authentication authentication,
            StudentProfileDTO submittedProfile,
            Model model) {

        try {

            String email =
                    authentication.getName();

            User user =
                    userService.getUserByEmail(email)

                            .orElseThrow(() ->
                                    new IllegalArgumentException(
                                            "User not found."
                                    )
                            );

            StudentProfileDTO databaseProfile =
                    studentService.getStudentProfile(
                            user.getId()
                    );

            // PRESERVE READ-ONLY INFORMATION

            submittedProfile.setId(
                    databaseProfile.getId()
            );

            submittedProfile.setRollNumber(
                    databaseProfile.getRollNumber()
            );

            submittedProfile.setEmail(
                    databaseProfile.getEmail()
            );

            submittedProfile.setEnabled(
                    databaseProfile.isEnabled()
            );

            submittedProfile.setCreatedAt(
                    databaseProfile.getCreatedAt()
            );

            // PRESERVE EXISTING IMAGE

            if (submittedProfile.getImagePath() == null
                    || submittedProfile.getImagePath().isBlank()) {

                submittedProfile.setImagePath(
                        databaseProfile.getImagePath()
                );

            }

        }

        catch (Exception ignored) {

        }

        model.addAttribute(
                "studentProfile",
                submittedProfile
        );

    }

}