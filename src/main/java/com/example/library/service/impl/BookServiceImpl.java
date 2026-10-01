package com.example.library.service.impl;

import com.example.library.service.BookService;
import com.example.library.dto.BookDTO;
import com.example.library.entity.Book;
import com.example.library.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book addBook(BookDTO dto) {

        validateBook(dto);

        if (bookRepository.existsByIsbn(dto.getIsbn().trim())) {
            throw new IllegalArgumentException(
                    "A book with this ISBN already exists."
            );
        }

        Book book = new Book();

        mapDtoToEntity(dto, book);

        book.setActive(true);

        return bookRepository.save(book);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDTO> getAllBooks() {

        return bookRepository.findAll()
                .stream()
                .map(this::convertToBookDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public BookDTO getBookById(Long id) {

        Book book = bookRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Book not found with ID: " + id)
                );

        return convertToBookDTO(book);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDTO> searchBooks(String searchTerm) {

        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAllBooks();
        }

        String term = searchTerm.trim();

        Map<Long, Book> uniqueBooks = new LinkedHashMap<>();

        bookRepository.findByIsbnContainingIgnoreCase(term)
                .forEach(book -> uniqueBooks.put(book.getId(), book));

        bookRepository.findByTitleContainingIgnoreCase(term)
                .forEach(book -> uniqueBooks.put(book.getId(), book));

        bookRepository.findByAuthorContainingIgnoreCase(term)
                .forEach(book -> uniqueBooks.put(book.getId(), book));

        bookRepository.findByCategoryContainingIgnoreCase(term)
                .forEach(book -> uniqueBooks.put(book.getId(), book));

        return uniqueBooks.values()
                .stream()
                .map(this::convertToBookDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Book updateBook(Long id, BookDTO dto) {

        validateBook(dto);

        Book book = bookRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Book not found with ID: " + id)
                );

        String isbn = dto.getIsbn().trim();

        if (!book.getIsbn().equalsIgnoreCase(isbn)
                && bookRepository.existsByIsbn(isbn)) {

            throw new IllegalArgumentException(
                    "A book with this ISBN already exists."
            );
        }

        mapDtoToEntity(dto, book);

        return bookRepository.save(book);
    }

    @Override
    public void toggleBookStatus(Long bookId) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Book not found with ID: " + bookId)
                );

        book.setActive(!book.isActive());

        bookRepository.save(book);
    }

    @Override
    public void deleteBook(Long bookId) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Book not found with ID: " + bookId)
                );

        bookRepository.delete(book);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDTO> getActiveBooks() {

        return bookRepository.findByActive(true)
                .stream()
                .map(this::convertToBookDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDTO> getAvailableBooks() {

        return bookRepository.findByActive(true)
                .stream()
                .filter(book -> book.getAvailableCopies() != null
                        && book.getAvailableCopies() > 0)
                .map(this::convertToBookDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getCategories() {
        return bookRepository.findDistinctCategories();
   }

    private void validateBook(BookDTO dto) {

        if (dto == null) {
            throw new IllegalArgumentException("Book details are required.");
        }

        if (dto.getTotalCopies() == null) {
            throw new IllegalArgumentException("Total copies are required.");
        }

        if (dto.getAvailableCopies() == null) {
            throw new IllegalArgumentException("Available copies are required.");
        }

        if (dto.getTotalCopies() < 0) {
            throw new IllegalArgumentException(
                    "Total copies cannot be negative."
            );
        }

        if (dto.getAvailableCopies() < 0) {
            throw new IllegalArgumentException(
                    "Available copies cannot be negative."
            );
        }

        if (dto.getAvailableCopies() > dto.getTotalCopies()) {
            throw new IllegalArgumentException(
                    "Available copies cannot be greater than total copies."
            );
        }
    }

    private void mapDtoToEntity(BookDTO dto, Book book) {

        book.setIsbn(dto.getIsbn().trim());
        book.setTitle(dto.getTitle().trim());
        book.setAuthor(dto.getAuthor().trim());
        book.setCategory(dto.getCategory().trim());
        book.setPublisher(dto.getPublisher().trim());
        book.setPublicationYear(dto.getPublicationYear());
        book.setLanguage(dto.getLanguage().trim());
        book.setTotalCopies(dto.getTotalCopies());
        book.setAvailableCopies(dto.getAvailableCopies());
        book.setDescription(
                dto.getDescription() != null
                        ? dto.getDescription().trim()
                        : null
        );
    }

    private BookDTO convertToBookDTO(Book book) {

        BookDTO dto = new BookDTO();

        dto.setId(book.getId());
        dto.setIsbn(book.getIsbn());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setCategory(book.getCategory());
        dto.setPublisher(book.getPublisher());
        dto.setPublicationYear(book.getPublicationYear());
        dto.setLanguage(book.getLanguage());
        dto.setTotalCopies(book.getTotalCopies());
        dto.setAvailableCopies(book.getAvailableCopies());
        dto.setDescription(book.getDescription());
        dto.setActive(book.isActive());

        return dto;
    }
}