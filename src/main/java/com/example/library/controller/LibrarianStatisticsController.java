package com.example.library.controller;

import com.example.library.dto.LibraryStatisticsDTO;
import com.example.library.service.LibraryStatisticsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/librarian")
public class LibrarianStatisticsController {

    private final LibraryStatisticsService libraryStatisticsService;

    public LibrarianStatisticsController(
            LibraryStatisticsService libraryStatisticsService) {
        this.libraryStatisticsService = libraryStatisticsService;
    }

    @GetMapping("/statistics")
    public String showStatistics(Model model) {

        LibraryStatisticsDTO statistics =
                libraryStatisticsService.getLibraryStatistics();

        model.addAttribute("statistics", statistics);

        return "library-statistics";
    }
}