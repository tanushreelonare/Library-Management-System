package com.example.library.controller;

import com.example.library.dto.StudentHistoryDTO;
import com.example.library.service.StudentHistoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/librarian")
public class LibrarianStudentHistoryController {

    private final StudentHistoryService
            studentHistoryService;

    public LibrarianStudentHistoryController(
            StudentHistoryService studentHistoryService) {

        this.studentHistoryService =
                studentHistoryService;
    }

    @GetMapping("/student-history")
    public String showStudentHistory(
            @RequestParam(
                    required = false
            )
            String search,
            Model model) {

        List<StudentHistoryDTO> history =
                studentHistoryService.getStudentHistory(
                        search
                );

        model.addAttribute(
                "history",
                history
        );

        model.addAttribute(
                "search",
                search != null
                        ? search
                        : ""
        );

        return "student-history";
    }
}