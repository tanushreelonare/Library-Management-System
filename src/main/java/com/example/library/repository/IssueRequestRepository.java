package com.example.library.repository;

import com.example.library.entity.IssueRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IssueRequestRepository extends JpaRepository<IssueRequest, Long> {

    @Query("""
        SELECT DISTINCT r
        FROM IssueRequest r
        LEFT JOIN FETCH r.items i
        LEFT JOIN FETCH i.book
        WHERE r.student.user.email = :email
        ORDER BY r.requestDate DESC
        """)
    List<IssueRequest> findByStudentEmail(
            @Param("email") String email
    );

    @Query("""
        SELECT DISTINCT r
        FROM IssueRequest r
        LEFT JOIN FETCH r.items i
        LEFT JOIN FETCH i.book
        WHERE r.id = :id
        AND r.student.user.email = :email
        """)
    Optional<IssueRequest> findByIdAndStudentEmail(
            @Param("id") Long id,
            @Param("email") String email
    );

    @Query("""
        SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END
        FROM IssueRequest r
        JOIN r.items i
        WHERE r.student.id = :studentId
        AND i.book.id = :bookId
        AND r.requestType = 'NEW_ISSUE'
        AND r.status = 'PENDING'
        AND i.status = 'PENDING'
        """)
    boolean existsPendingRequestForBook(
            @Param("studentId") Long studentId,
            @Param("bookId") Long bookId
    );

    @Query("""
        SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END
        FROM IssueRequestItem i
        WHERE i.issueRequest.student.id = :studentId
        AND i.issueRequest.requestType = 'NEW_ISSUE'
        AND i.status IN ('PENDING', 'ISSUED')
        """)
    long countActiveBookRequests(
            @Param("studentId") Long studentId
    );

    @Query("""
        SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END
        FROM IssueRequestItem i
        WHERE i.reissueOfItem.id = :itemId
        AND i.issueRequest.requestType = 'REISSUE'
        AND i.status = 'PENDING'
        """)
    boolean existsPendingReissueRequest(
            @Param("itemId") Long itemId
    );

    @Query("""
        SELECT DISTINCT r
        FROM IssueRequest r
        LEFT JOIN FETCH r.items i
        LEFT JOIN FETCH i.book
        LEFT JOIN FETCH r.student
        WHERE r.status IN ('PENDING', 'PARTIALLY_APPROVED')
        ORDER BY r.requestDate ASC
        """)
    List<IssueRequest> findAllPendingRequests();

    @Query("""
        SELECT DISTINCT r
        FROM IssueRequest r
        LEFT JOIN FETCH r.items i
        LEFT JOIN FETCH i.book
        LEFT JOIN FETCH r.student
        WHERE r.id = :id
        """)
    Optional<IssueRequest> findRequestForLibrarian(
            @Param("id") Long id
    );

    @Query("""
        SELECT DISTINCT r
        FROM IssueRequest r
        LEFT JOIN FETCH r.items i
        LEFT JOIN FETCH i.book
        LEFT JOIN FETCH r.student
        ORDER BY r.requestDate DESC
        """)
    List<IssueRequest> findAllRequestsForLibrarian();
}