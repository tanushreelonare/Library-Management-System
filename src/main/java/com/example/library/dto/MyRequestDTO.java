package com.example.library.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MyRequestDTO {

    private Long requestId;
    private String studentName;
    private String rollNumber;
    private LocalDateTime requestDate;
    private String status;
    private String requestType;

    private List<RequestItemDTO> items = new ArrayList<>();

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
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

    public List<RequestItemDTO> getItems() {
        return items;
    }

    public void setItems(List<RequestItemDTO> items) {
        this.items = items;
    }

    public static class RequestItemDTO {

        private Long itemId;
        private Long bookId;
        private String bookTitle;
        private String author;
        private String isbn;

        private Integer durationDays;

        private String status;

        private LocalDateTime approvedAt;
        private LocalDateTime issuedAt;
        private LocalDateTime dueDate;
        private LocalDateTime returnedAt;

        private String rejectionReason;

        private Long reissueOfItemId;
        private LocalDateTime currentDueDate;

        public Long getItemId() {
            return itemId;
        }

        public void setItemId(Long itemId) {
            this.itemId = itemId;
        }

        public Long getBookId() {
            return bookId;
        }

        public void setBookId(Long bookId) {
            this.bookId = bookId;
        }

        public String getBookTitle() {
            return bookTitle;
        }

        public void setBookTitle(String bookTitle) {
            this.bookTitle = bookTitle;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getIsbn() {
            return isbn;
        }

        public void setIsbn(String isbn) {
            this.isbn = isbn;
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

        public Long getReissueOfItemId() {
            return reissueOfItemId;
        }

        public void setReissueOfItemId(Long reissueOfItemId) {
            this.reissueOfItemId = reissueOfItemId;
        }

        public LocalDateTime getCurrentDueDate() {
            return currentDueDate;
        }

        public void setCurrentDueDate(LocalDateTime currentDueDate) {
            this.currentDueDate = currentDueDate;
        }
    }
}