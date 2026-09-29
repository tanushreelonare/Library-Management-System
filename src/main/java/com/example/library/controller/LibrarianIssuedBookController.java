package com.example.library.controller;

import com.example.library.dto.IssuedBookDTO;
import com.example.library.service.IssuedBookService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/librarian/issued-books")
public class LibrarianIssuedBookController {

    private final IssuedBookService issuedBookService;

    public LibrarianIssuedBookController(
            IssuedBookService issuedBookService) {

        this.issuedBookService =
                issuedBookService;
    }

    // Display issued book report

    @GetMapping
    public String getIssuedBooks(Model model) {

        List<IssuedBookDTO> issuedBooks =
                issuedBookService.getAllIssuedBooks();

        model.addAttribute(
                "issuedBooks",
                issuedBooks
        );

        return "librarian-issued-books";
    }
}