package com.example.library.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "issue_requests")
public class IssueRequest extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(nullable = false)
    private LocalDateTime requestDate;

    @Column(nullable = false, length = 20)
    private String status = "PENDING";

    @Column(nullable = false, length = 20)
    private String requestType = "NEW_ISSUE";

    @OneToMany(
            mappedBy = "issueRequest",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<IssueRequestItem> items = new ArrayList<>();

    @PrePersist
    protected void onRequestCreate() {

        if (requestDate == null) {
            requestDate = LocalDateTime.now();
        }

        if (status == null || status.isBlank()) {
            status = "PENDING";
        }

        if (requestType == null || requestType.isBlank()) {
            requestType = "NEW_ISSUE";
        }
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public List<IssueRequestItem> getItems() {
        return items;
    }

    public void setItems(List<IssueRequestItem> items) {
        this.items = items;
    }

    public void addItem(IssueRequestItem item) {
        items.add(item);
        item.setIssueRequest(this);
    }

    public void removeItem(IssueRequestItem item) {
        items.remove(item);
        item.setIssueRequest(null);
    }
}