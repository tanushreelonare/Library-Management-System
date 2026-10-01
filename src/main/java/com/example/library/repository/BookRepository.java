package com.example.library.repository;

import com.example.library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIsbn(String isbn);

    boolean existsByIsbn(String isbn);

    List<Book> findByTitleContainingIgnoreCase(String title);

    List<Book> findByAuthorContainingIgnoreCase(String author);

    List<Book> findByCategoryContainingIgnoreCase(String category);

    List<Book> findByIsbnContainingIgnoreCase(String isbn);

    List<Book> findByActive(boolean active);

    @Query("""
            SELECT DISTINCT b.category
            FROM Book b
            WHERE b.category IS NOT NULL
              AND TRIM(b.category) <> ''
            ORDER BY b.category
            """)
    List<String> findDistinctCategories();
}