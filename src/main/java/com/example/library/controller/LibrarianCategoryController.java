package com.example.library.controller;

import com.example.library.dto.BookDTO;
import com.example.library.service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/librarian")
public class LibrarianCategoryController {

    private final BookService bookService;

    public LibrarianCategoryController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/categories")
    public String showCategories(
            @RequestParam(required = false) String category,
            Model model) {

        List<String> categories = bookService.getCategories();

        model.addAttribute("categories", categories);
        model.addAttribute(
                "selectedCategory",
                category != null ? category : ""
        );

        if (category != null && !category.isBlank()) {

            List<BookDTO> books =
                    bookService.searchBooks(category.trim());

            books = books.stream()
                    .filter(book ->
                            book.getCategory() != null
                                    && book.getCategory()
                                    .equalsIgnoreCase(category.trim())
                    )
                    .toList();

            model.addAttribute("books", books);

        } else {

            model.addAttribute("books", List.of());

        }

        return "librarian-categories";
    }
}