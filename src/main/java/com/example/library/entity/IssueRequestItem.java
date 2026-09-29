package com.example.library.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "issue_request_items")
public class IssueRequestItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "issue_request_id", nullable = false)
    private IssueRequest issueRequest;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(nullable = false)
    private Integer durationDays;

    @Column(nullable = false, length = 20)
    private String status = "PENDING";

    @Column(nullable = false, length = 20)
    private String requestType = "NEW_ISSUE";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reissue_of_item_id")
    private IssueRequestItem reissueOfItem;

    private LocalDateTime approvedAt;

    private LocalDateTime issuedAt;

    private LocalDateTime dueDate;

    private LocalDateTime returnedAt;

    @Column(length = 500)
    private String rejectionReason;

    public IssueRequest getIssueRequest() {
        return issueRequest;
    }

    public void setIssueRequest(IssueRequest issueRequest) {
        this.issueRequest = issueRequest;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Integer getDurationDays() {
        return durationDays;
    }

    public void setDurationDays(Integer durationDays) {
        this.durationDays = durationDays;
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

    public IssueRequestItem getReissueOfItem() {
        return reissueOfItem;
    }

    public void setReissueOfItem(IssueRequestItem reissueOfItem) {
        this.reissueOfItem = reissueOfItem;
    }

    public LocalDateTime getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(LocalDateTime approvedAt) {
        this.approvedAt = approvedAt;
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(LocalDateTime issuedAt) {
        this.issuedAt = issuedAt;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDateTime getReturnedAt() {
        return returnedAt;
    }

    public void setReturnedAt(LocalDateTime returnedAt) {
        this.returnedAt = returnedAt;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }
}