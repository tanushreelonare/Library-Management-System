package com.example.library.service.impl;

import com.example.library.dto.IssuedBookDTO;
import com.example.library.entity.Book;
import com.example.library.entity.IssueRequest;
import com.example.library.entity.IssueRequestItem;
import com.example.library.repository.BookRepository;
import com.example.library.repository.IssueRequestItemRepository;
import com.example.library.repository.IssueRequestRepository;
import com.example.library.service.StudentManageRecordsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class StudentManageRecordsServiceImpl
        implements StudentManageRecordsService {

    private final IssueRequestItemRepository
            issueRequestItemRepository;

    private final IssueRequestRepository
            issueRequestRepository;

    private final BookRepository
            bookRepository;

    public StudentManageRecordsServiceImpl(
            IssueRequestItemRepository issueRequestItemRepository,
            IssueRequestRepository issueRequestRepository,
            BookRepository bookRepository) {

        this.issueRequestItemRepository =
                issueRequestItemRepository;

        this.issueRequestRepository =
                issueRequestRepository;

        this.bookRepository =
                bookRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<IssuedBookDTO> getStudentIssuedBooks(
            String email) {

        List<IssueRequestItem> issuedItems =
                issueRequestItemRepository
                        .findIssuedBooksByStudentEmail(email);

        List<IssuedBookDTO> issuedBooks =
                new ArrayList<>();

        for (IssueRequestItem item : issuedItems) {

            issuedBooks.add(
                    convertToDTO(item)
            );
        }

        return issuedBooks;
    }

    @Override
    public void returnBook(
            Long itemId,
            String email) {

        IssueRequestItem item =
                issueRequestItemRepository
                        .findById(itemId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Issued book record not found."
                                ));

        if (item.getIssueRequest() == null
                || item.getIssueRequest().getStudent() == null
                || item.getIssueRequest()
                        .getStudent()
                        .getUser() == null) {

            throw new IllegalArgumentException(
                    "Student information not found."
            );
        }

        String studentEmail =
                item.getIssueRequest()
                        .getStudent()
                        .getUser()
                        .getEmail();

        if (studentEmail == null
                || !studentEmail.equalsIgnoreCase(email)) {

            throw new IllegalArgumentException(
                    "You are not authorized to return this book."
            );
        }

        if (!"ISSUED".equals(item.getStatus())) {

            throw new IllegalArgumentException(
                    "This book is not currently issued."
            );
        }

        Book book = item.getBook();

        if (book == null) {

            throw new IllegalArgumentException(
                    "Book information not found."
            );
        }

        // Mark the book as returned.

        item.setStatus("RETURNED");

        item.setReturnedAt(
                LocalDateTime.now()
        );

        issueRequestItemRepository.save(item);

        // Increase available copies after return.

        int availableCopies =
                book.getAvailableCopies() != null
                        ? book.getAvailableCopies()
                        : 0;

        int totalCopies =
                book.getTotalCopies() != null
                        ? book.getTotalCopies()
                        : 0;

        if (availableCopies < totalCopies) {

            book.setAvailableCopies(
                    availableCopies + 1
            );

        } else {

            throw new IllegalArgumentException(
                    "Available copies cannot exceed total copies."
            );
        }

        bookRepository.save(book);
    }

    @Override
    public void requestReissue(
            Long itemId,
            Integer durationDays,
            String email) {

        if (itemId == null) {

            throw new IllegalArgumentException(
                    "Issued book record not found."
            );
        }

        if (durationDays == null
                || !(durationDays == 7
                || durationDays == 14
                || durationDays == 21
                || durationDays == 30)) {

            throw new IllegalArgumentException(
                    "Please select a valid re-issue duration."
            );
        }

        IssueRequestItem existingItem =
                issueRequestItemRepository
                        .findById(itemId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Issued book record not found."
                                ));

        if (existingItem.getIssueRequest() == null
                || existingItem.getIssueRequest().getStudent() == null
                || existingItem.getIssueRequest()
                        .getStudent()
                        .getUser() == null) {

            throw new IllegalArgumentException(
                    "Student information not found."
            );
        }

        String studentEmail =
                existingItem.getIssueRequest()
                        .getStudent()
                        .getUser()
                        .getEmail();

        if (studentEmail == null
                || !studentEmail.equalsIgnoreCase(email)) {

            throw new IllegalArgumentException(
                    "You are not authorized to request a re-issue for this book."
            );
        }

        if (!"ISSUED".equals(existingItem.getStatus())) {

            throw new IllegalArgumentException(
                    "Only currently issued books can be re-issued."
            );
        }

        if (existingItem.getDueDate() == null) {

            throw new IllegalArgumentException(
                    "Current due date is not available."
            );
        }

        if (issueRequestItemRepository
                .existsPendingReissueRequest(itemId)) {

            throw new IllegalArgumentException(
                    "A re-issue request for this book is already pending."
            );
        }

        Book book = existingItem.getBook();

        if (book == null) {

            throw new IllegalArgumentException(
                    "Book information not found."
            );
        }

        // Create a new re-issue request.

        IssueRequest reissueRequest =
                new IssueRequest();

        reissueRequest.setStudent(
                existingItem.getIssueRequest()
                        .getStudent()
        );

        reissueRequest.setStatus(
                "PENDING"
        );

        // Create the re-issue request item.

        IssueRequestItem reissueItem =
                new IssueRequestItem();

        reissueItem.setBook(book);

        reissueItem.setDurationDays(
                durationDays
        );

        reissueItem.setStatus(
                "PENDING"
        );

        reissueItem.setRequestType(
                "REISSUE"
        );

        reissueItem.setReissueOfItem(
                existingItem
        );

        reissueRequest.addItem(
                reissueItem
        );

        issueRequestRepository.save(
                reissueRequest
        );
    }

    private IssuedBookDTO convertToDTO(
            IssueRequestItem item) {

        IssuedBookDTO dto =
                new IssuedBookDTO();

        dto.setItemId(
                item.getId()
        );

        if (item.getBook() != null) {

            dto.setBookId(
                    item.getBook().getId()
            );

            dto.setBookTitle(
                    item.getBook().getTitle()
            );

            dto.setAuthor(
                    item.getBook().getAuthor()
            );

            dto.setIsbn(
                    item.getBook().getIsbn()
            );
        }

        if (item.getIssueRequest() != null
                && item.getIssueRequest().getStudent() != null) {

            dto.setStudentName(
                    item.getIssueRequest()
                            .getStudent()
                            .getStudentName()
            );

            dto.setRollNumber(
                    item.getIssueRequest()
                            .getStudent()
                            .getRollNumber()
            );
        }

        dto.setDurationDays(
                item.getDurationDays()
        );

        dto.setIssuedAt(
                item.getIssuedAt()
        );

        dto.setDueDate(
                item.getDueDate()
        );

        dto.setReturnedAt(
                item.getReturnedAt()
        );

        dto.setStatus(
                item.getStatus()
        );

        return dto;
    }
}