package com.example.library.repository;

import com.example.library.entity.IssueRequestItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentHistoryRepository
        extends JpaRepository<IssueRequestItem, Long> {

    @Query("""
            SELECT i
            FROM IssueRequestItem i
            JOIN FETCH i.issueRequest r
            JOIN FETCH r.student s
            JOIN FETCH i.book b
            JOIN FETCH s.user u
            WHERE
                :search IS NULL
                OR :search = ''
                OR LOWER(s.studentName) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(s.rollNumber) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%'))
            ORDER BY r.requestDate DESC, i.id DESC
            """)
    List<IssueRequestItem> findStudentHistory(
            @Param("search") String search
    );
}