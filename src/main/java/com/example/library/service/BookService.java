package com.example.library.service;

import com.example.library.dto.BookDTO;
import com.example.library.entity.Book;

import java.util.List;

public interface BookService {

    // Add a new book
    Book addBook(BookDTO dto);

    // Get all books
    List<BookDTO> getAllBooks();

    // Get a book by ID
    BookDTO getBookById(Long id);

    // Search books
    List<BookDTO> searchBooks(String searchTerm);

    // Update an existing book
    Book updateBook(Long id, BookDTO dto);

    // Activate or deactivate a book
    void toggleBookStatus(Long bookId);

    // Delete a book permanently
    void deleteBook(Long bookId);

    // Get active books
    List<BookDTO> getActiveBooks();

    // Get available books
    List<BookDTO> getAvailableBooks();
}