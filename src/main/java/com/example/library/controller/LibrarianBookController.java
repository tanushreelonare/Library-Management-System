package com.example.library.controller;

import com.example.library.dto.BookDTO;
import com.example.library.entity.Book;
import com.example.library.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/librarian/books")
public class LibrarianBookController {

    private final BookService bookService;

    public LibrarianBookController(BookService bookService) {
        this.bookService = bookService;
    }

    // Display all books
    @GetMapping
    public String getBooks(
            @RequestParam(required = false) String search,
            org.springframework.ui.Model model) {

        List<BookDTO> books;

        if (search != null && !search.trim().isEmpty()) {
            books = bookService.searchBooks(search);
        } else {
            books = bookService.getAllBooks();
        }

        model.addAttribute("books", books);
        model.addAttribute("search", search);

        return "librarian-books";
    }

    // Display add book page
    @GetMapping("/add")
    public String showAddBookPage() {
        return "librarian-add-book";
    }

    // Get a single book
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {

        return ResponseEntity.ok(bookService.getBookById(id));
    }

    // Add a new book
    @PostMapping
    @ResponseBody
    public ResponseEntity<?> addBook(
            @Valid @RequestBody BookDTO dto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(
                    bindingResult.getFieldErrors()
                            .stream()
                            .map(error -> error.getDefaultMessage())
                            .toList()
            );
        }

        try {
            Book book = bookService.addBook(dto);
            return ResponseEntity.ok(book);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

    // Update an existing book
    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> updateBook(
            @PathVariable Long id,
            @Valid @RequestBody BookDTO dto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(
                    bindingResult.getFieldErrors()
                            .stream()
                            .map(error -> error.getDefaultMessage())
                            .toList()
            );
        }

        try {
            Book book = bookService.updateBook(id, dto);
            return ResponseEntity.ok(book);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

    // Activate or deactivate a book
    @PatchMapping("/{id}/toggle-status")
    @ResponseBody
    public ResponseEntity<?> toggleBookStatus(
            @PathVariable Long id) {

        try {
            bookService.toggleBookStatus(id);

            return ResponseEntity.ok(
                    bookService.getBookById(id)
            );

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

    // Delete a book permanently
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteBook(
            @PathVariable Long id) {

        try {
            bookService.deleteBook(id);
            return ResponseEntity.ok().build();

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }
}