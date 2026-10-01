package com.example.library.repository;

import com.example.library.entity.Book;
import com.example.library.entity.IssueRequest;
import com.example.library.entity.IssueRequestItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LibraryStatisticsRepository
        extends JpaRepository<Book, Long> {

    /* Total Books */

    @Query("""
            SELECT COUNT(b)
            FROM Book b
            """)
    long countTotalBooks();


    /* Total Copies */

    @Query("""
            SELECT COALESCE(SUM(b.totalCopies), 0)
            FROM Book b
            """)
    long sumTotalCopies();


    /* Available Copies */

    @Query("""
            SELECT COALESCE(SUM(b.availableCopies), 0)
            FROM Book b
            """)
    long sumAvailableCopies();


    /* All Books For Category Statistics */

    @Query("""
            SELECT b
            FROM Book b
            ORDER BY b.category, b.title
            """)
    List<Book> findAllBooksForStatistics();


    /* Currently Issued And Returned Books */

    @Query("""
            SELECT DISTINCT i
            FROM IssueRequestItem i
            JOIN FETCH i.issueRequest r
            JOIN FETCH r.student s
            JOIN FETCH i.book b
            WHERE i.status IN ('ISSUED', 'RETURNED')
            ORDER BY i.issuedAt DESC
            """)
    List<IssueRequestItem> findCirculationItems();


    /* All Request Records */

    @Query("""
            SELECT DISTINCT r
            FROM IssueRequest r
            LEFT JOIN FETCH r.student s
            LEFT JOIN FETCH r.items i
            LEFT JOIN FETCH i.book b
            ORDER BY r.requestDate DESC
            """)
    List<IssueRequest> findAllRequestsForStatistics();


    /* Total Students */

    @Query("""
            SELECT COUNT(s)
            FROM Student s
            """)
    long countTotalStudents();
}