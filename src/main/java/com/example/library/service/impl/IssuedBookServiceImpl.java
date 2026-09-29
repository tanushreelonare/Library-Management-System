package com.example.library.service.impl;

import com.example.library.dto.IssuedBookDTO;
import com.example.library.entity.IssueRequestItem;
import com.example.library.repository.IssueRequestItemRepository;
import com.example.library.service.IssuedBookService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class IssuedBookServiceImpl implements IssuedBookService {

    private final IssueRequestItemRepository issueRequestItemRepository;

    public IssuedBookServiceImpl(
            IssueRequestItemRepository issueRequestItemRepository) {

        this.issueRequestItemRepository =
                issueRequestItemRepository;
    }

    // Get all currently issued books

    @Override
    public List<IssuedBookDTO> getAllIssuedBooks() {

        List<IssueRequestItem> issuedItems =
                issueRequestItemRepository.findAllIssuedBooks();

        List<IssuedBookDTO> issuedBooks =
                new ArrayList<>();

        for (IssueRequestItem item : issuedItems) {

            issuedBooks.add(
                    convertToDTO(item)
            );
        }

        return issuedBooks;
    }

    // Convert issued book entity to DTO

    private IssuedBookDTO convertToDTO(
            IssueRequestItem item) {

        IssuedBookDTO dto =
                new IssuedBookDTO();

        dto.setItemId(
                item.getId()
        );

        // Book information

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

        // Student information

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

        // Issue information

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