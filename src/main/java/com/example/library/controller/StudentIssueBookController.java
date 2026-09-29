package com.example.library.controller;

import com.example.library.dto.BookDTO;
import com.example.library.service.BookService;
import com.example.library.service.IssueRequestService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/student/issue-books")
public class StudentIssueBookController {

    private static final String SELECTED_BOOK_IDS =
            "studentSelectedBookIds";

    private static final String BOOK_DURATIONS =
            "studentBookDurations";

    private final BookService bookService;

    private final IssueRequestService issueRequestService;

    public StudentIssueBookController(
            BookService bookService,
            IssueRequestService issueRequestService) {

        this.bookService = bookService;

        this.issueRequestService =
                issueRequestService;
    }

    @GetMapping
    public String showIssueBooksPage(
            @RequestParam(required = false) Long bookId,
            HttpSession session,
            Model model) {

        Set<Long> selectedBookIds =
                getSelectedBookIds(session);

        boolean maximumLimitReached = false;

        if (bookId != null) {

            maximumLimitReached =
                    !addBookToSelection(
                            bookId,
                            selectedBookIds,
                            session);
        }

        List<BookDTO> selectedBooks =
                new ArrayList<>();

        for (Long id : selectedBookIds) {

            try {

                BookDTO book =
                        bookService.getBookById(id);

                if (book.isActive()
                        && book.getAvailableCopies() > 0) {

                    selectedBooks.add(book);
                }

            } catch (IllegalArgumentException ignored) {
                // Ignore books that no longer exist.
            }
        }

        removeUnavailableBooks(
                selectedBooks,
                selectedBookIds,
                session);

        Map<Long, Integer> durations =
                getBookDurations(session);

        for (BookDTO book : selectedBooks) {

            durations.putIfAbsent(
                    book.getId(),
                    14);
        }

        session.setAttribute(
                BOOK_DURATIONS,
                durations);

        model.addAttribute(
                "selectedBooks",
                selectedBooks);

        model.addAttribute(
                "durations",
                durations);

        model.addAttribute(
                "maximumLimitReached",
                maximumLimitReached);

        return "student-issue-books";
    }

    @PostMapping("/remove")
    public String removeBook(
            @RequestParam Long bookId,
            HttpSession session) {

        Set<Long> selectedBookIds =
                getSelectedBookIds(session);

        selectedBookIds.remove(bookId);

        session.setAttribute(
                SELECTED_BOOK_IDS,
                selectedBookIds);

        Map<Long, Integer> durations =
                getBookDurations(session);

        durations.remove(bookId);

        session.setAttribute(
                BOOK_DURATIONS,
                durations);

        return "redirect:/student/issue-books";
    }

    @PostMapping("/update")
    public String updateDurations(
            @RequestParam(required = false)
            List<Long> bookIds,

            @RequestParam(required = false)
            List<Integer> durations,

            HttpSession session) {

        Map<Long, Integer> durationMap =
                getBookDurations(session);

        if (bookIds != null
                && durations != null) {

            int size =
                    Math.min(
                            bookIds.size(),
                            durations.size());

            for (int i = 0; i < size; i++) {

                Long bookId =
                        bookIds.get(i);

                Integer duration =
                        durations.get(i);

                if (duration != null
                        && isValidDuration(duration)) {

                    durationMap.put(
                            bookId,
                            duration);
                }
            }
        }

        session.setAttribute(
                BOOK_DURATIONS,
                durationMap);

        return "redirect:/student/issue-books";
    }

    @PostMapping("/submit")
    public String submitIssueRequest(
            HttpSession session,
            Model model) {

        Map<Long, Integer> durations =
                getBookDurations(session);

        try {

            issueRequestService.submitIssueRequest(
                    getAuthenticatedEmail(session),
                    durations);

            session.removeAttribute(
                    SELECTED_BOOK_IDS);

            session.removeAttribute(
                    BOOK_DURATIONS);

            return "redirect:/student/requests?submitted=true";

        } catch (RuntimeException e) {

            model.addAttribute(
                    "selectedBooks",
                    getSelectedBooks(session));

            model.addAttribute(
                    "durations",
                    durations);

            if ("You can have a maximum of 6 active books"
                    .equals(e.getMessage())
                    || "You can have a maximum of 6 books"
                    .equals(e.getMessage())) {

                model.addAttribute(
                        "maximumLimitReached",
                        true);

            } else {

                model.addAttribute(
                        "errorMessage",
                        e.getMessage());
            }

            return "student-issue-books";
        }
    }

    private String getAuthenticatedEmail(
            HttpSession session) {

        org.springframework.security.core.Authentication
                authentication =
                org.springframework.security.core.context
                        .SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()) {

            throw new IllegalArgumentException(
                    "Please login again.");
        }

        return authentication.getName();
    }

    private List<BookDTO> getSelectedBooks(
            HttpSession session) {

        Set<Long> selectedBookIds =
                getSelectedBookIds(session);

        List<BookDTO> books =
                new ArrayList<>();

        for (Long id : selectedBookIds) {

            try {

                BookDTO book =
                        bookService.getBookById(id);

                if (book.isActive()
                        && book.getAvailableCopies() > 0) {

                    books.add(book);
                }

            } catch (IllegalArgumentException ignored) {
                // Ignore invalid book IDs.
            }
        }

        return books;
    }

    private boolean addBookToSelection(
            Long bookId,
            Set<Long> selectedBookIds,
            HttpSession session) {

        try {

            BookDTO book =
                    bookService.getBookById(bookId);

            if (book.isActive()
                    && book.getAvailableCopies() > 0) {

                if (selectedBookIds.size()
                        >= 6
                        && !selectedBookIds.contains(bookId)) {

                    return false;
                }

                selectedBookIds.add(bookId);

                session.setAttribute(
                        SELECTED_BOOK_IDS,
                        selectedBookIds);

                return true;
            }

        } catch (IllegalArgumentException ignored) {
            // Ignore invalid book IDs.
        }

        return true;
    }

    private void removeUnavailableBooks(
            List<BookDTO> selectedBooks,
            Set<Long> selectedBookIds,
            HttpSession session) {

        Set<Long> validIds =
                new LinkedHashSet<>();

        for (BookDTO book : selectedBooks) {

            validIds.add(book.getId());
        }

        selectedBookIds.retainAll(validIds);

        session.setAttribute(
                SELECTED_BOOK_IDS,
                selectedBookIds);
    }

    private boolean isValidDuration(
            Integer duration) {

        return duration == 7
                || duration == 14
                || duration == 21
                || duration == 30;
    }

    @SuppressWarnings("unchecked")
    private Set<Long> getSelectedBookIds(
            HttpSession session) {

        Object value =
                session.getAttribute(
                        SELECTED_BOOK_IDS);

        if (value instanceof Set<?>) {

            return new LinkedHashSet<>(
                    (Set<Long>) value);
        }

        return new LinkedHashSet<>();
    }

    @SuppressWarnings("unchecked")
    private Map<Long, Integer> getBookDurations(
            HttpSession session) {

        Object value =
                session.getAttribute(
                        BOOK_DURATIONS);

        if (value instanceof Map<?, ?>) {

            return new LinkedHashMap<>(
                    (Map<Long, Integer>) value);
        }

        return new LinkedHashMap<>();
    }
}