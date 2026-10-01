package com.example.library.repository;

import com.example.library.entity.IssueRequestItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FineReportRepository
        extends JpaRepository<IssueRequestItem, Long> {

    @Query("""
            SELECT DISTINCT i
            FROM IssueRequestItem i
            JOIN FETCH i.issueRequest r
            JOIN FETCH r.student s
            JOIN FETCH i.book b
            WHERE i.status IN ('ISSUED', 'RETURNED')
            AND i.dueDate IS NOT NULL
            ORDER BY i.dueDate ASC
            """)
    List<IssueRequestItem> findFineReportItems();
}