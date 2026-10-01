package com.example.library.repository;

import com.example.library.entity.DonationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DonationRequestRepository
        extends JpaRepository<DonationRequest, Long> {

    /* Student donation requests by student ID */

    @Query("""
            SELECT d
            FROM DonationRequest d
            JOIN FETCH d.student s
            WHERE s.id = :studentId
            ORDER BY d.requestDate DESC, d.id DESC
            """)
    List<DonationRequest> findByStudentId(
            @Param("studentId") Long studentId
    );


    /* Student donation requests by login email */

    @Query("""
            SELECT d
            FROM DonationRequest d
            JOIN FETCH d.student s
            JOIN FETCH s.user u
            WHERE u.email = :email
            ORDER BY d.requestDate DESC, d.id DESC
            """)
    List<DonationRequest> findByStudentEmail(
            @Param("email") String email
    );


    /* All donation requests for librarian */

    @Query("""
            SELECT d
            FROM DonationRequest d
            JOIN FETCH d.student s
            ORDER BY d.requestDate DESC, d.id DESC
            """)
    List<DonationRequest> findAllForLibrarian();


    /* Filter donation requests by status */

    @Query("""
            SELECT d
            FROM DonationRequest d
            JOIN FETCH d.student s
            WHERE d.status = :status
            ORDER BY d.requestDate DESC, d.id DESC
            """)
    List<DonationRequest> findByStatus(
            @Param("status") String status
    );


    /* Search donation requests for librarian */

    @Query("""
            SELECT d
            FROM DonationRequest d
            JOIN FETCH d.student s
            JOIN FETCH s.user u
            WHERE
                :search IS NULL
                OR :search = ''
                OR LOWER(s.studentName) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(s.rollNumber) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(d.bookName) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(d.author) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(d.category) LIKE LOWER(CONCAT('%', :search, '%'))
            ORDER BY d.requestDate DESC, d.id DESC
            """)
    List<DonationRequest> searchForLibrarian(
            @Param("search") String search
    );


    /* Search donation requests by student and status */

    @Query("""
            SELECT d
            FROM DonationRequest d
            JOIN FETCH d.student s
            WHERE s.id = :studentId
              AND d.status = :status
            ORDER BY d.requestDate DESC, d.id DESC
            """)
    List<DonationRequest> findByStudentIdAndStatus(
            @Param("studentId") Long studentId,
            @Param("status") String status
    );


    /* Pending donation requests */

    @Query("""
            SELECT d
            FROM DonationRequest d
            JOIN FETCH d.student s
            WHERE d.status = 'PENDING'
            ORDER BY d.requestDate ASC, d.id ASC
            """)
    List<DonationRequest> findPendingRequests();
}