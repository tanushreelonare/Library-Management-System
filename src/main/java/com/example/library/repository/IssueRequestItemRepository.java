package com.example.library.repository;

import com.example.library.entity.IssueRequestItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IssueRequestItemRepository
        extends JpaRepository<IssueRequestItem, Long> {

    // Find all currently issued books for librarian report

    @Query("""
            SELECT DISTINCT i
            FROM IssueRequestItem i
            JOIN FETCH i.book
            JOIN FETCH i.issueRequest r
            JOIN FETCH r.student
            WHERE i.status = 'ISSUED'
            ORDER BY i.issuedAt DESC
            """)
    List<IssueRequestItem> findAllIssuedBooks();


    // Find currently issued books belonging to one student

    @Query("""
            SELECT DISTINCT i
            FROM IssueRequestItem i
            JOIN FETCH i.book
            JOIN FETCH i.issueRequest r
            JOIN FETCH r.student s
            JOIN FETCH s.user u
            WHERE i.status = 'ISSUED'
            AND u.email = :email
            ORDER BY i.issuedAt DESC
            """)
    List<IssueRequestItem> findIssuedBooksByStudentEmail(
            @Param("email") String email);

    @Query("""
        SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END
        FROM IssueRequestItem i
        WHERE i.reissueOfItem.id = :itemId
        AND i.requestType = 'REISSUE'
        AND i.status = 'PENDING'
        """)
        boolean existsPendingReissueRequest(
                @Param("itemId") Long itemId
        );    
}