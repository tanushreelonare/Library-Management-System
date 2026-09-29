package com.example.library.controller;

import com.example.library.dto.BookDTO;
import com.example.library.service.BookService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/student/books")
public class StudentBookController {

    private static final String SELECTED_BOOK_IDS = "studentSelectedBookIds";
    private static final String BOOK_DURATIONS = "studentBookDurations";

    private final BookService bookService;

    public StudentBookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public String getAvailableBooks(
            @RequestParam(required = false) String search,
            HttpSession session,
            Model model) {

        List<BookDTO> books;

        if (search != null && !search.trim().isEmpty()) {

            books = bookService.searchBooks(search)
                    .stream()
                    .filter(book ->
                            book.isActive()
                                    && book.getAvailableCopies() > 0)
                    .toList();

        } else {

            books = bookService.getAvailableBooks();

        }

        Set<Long> selectedBookIds = getSelectedBookIds(session);

        model.addAttribute("books", books);
        model.addAttribute("search", search);
        model.addAttribute("selectedBookIds", selectedBookIds);

        return "student-available-books";
    }

    @PostMapping("/select")
    public String selectBooks(
            @RequestParam(required = false) List<Long> bookIds,
            HttpSession session) {

        Set<Long> selectedBookIds = getSelectedBookIds(session);

        if (bookIds != null) {

            for (Long bookId : bookIds) {

                try {

                    BookDTO book = bookService.getBookById(bookId);

                    if (book.isActive()
                            && book.getAvailableCopies() > 0) {

                        selectedBookIds.add(bookId);

                    }

                } catch (IllegalArgumentException ignored) {
                    // Ignore invalid book IDs.
                }
            }
        }

        session.setAttribute(SELECTED_BOOK_IDS, selectedBookIds);

        return "redirect:/student/issue-books";
    }

    @GetMapping("/add/{bookId}")
    public String addSingleBook(
            @PathVariable Long bookId,
            HttpSession session) {

        try {

            BookDTO book = bookService.getBookById(bookId);

            if (book.isActive()
                    && book.getAvailableCopies() > 0) {

                Set<Long> selectedBookIds =
                        getSelectedBookIds(session);

                selectedBookIds.add(bookId);

                session.setAttribute(
                        SELECTED_BOOK_IDS,
                        selectedBookIds);
            }

        } catch (IllegalArgumentException ignored) {
            // Invalid book IDs are ignored.
        }

        return "redirect:/student/issue-books";
    }

    @PostMapping("/clear-selection")
    public String clearSelection(HttpSession session) {

        session.removeAttribute(SELECTED_BOOK_IDS);
        session.removeAttribute(BOOK_DURATIONS);

        return "redirect:/student/books";
    }

    @SuppressWarnings("unchecked")
    private Set<Long> getSelectedBookIds(HttpSession session) {

        Object value = session.getAttribute(SELECTED_BOOK_IDS);

        if (value instanceof Set<?>) {

            return new LinkedHashSet<>((Set<Long>) value);

        }

        return new LinkedHashSet<>();
    }
}