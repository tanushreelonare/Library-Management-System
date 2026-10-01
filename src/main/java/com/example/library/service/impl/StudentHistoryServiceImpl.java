package com.example.library.service.impl;

import com.example.library.dto.StudentHistoryDTO;
import com.example.library.entity.IssueRequestItem;
import com.example.library.repository.StudentHistoryRepository;
import com.example.library.service.StudentHistoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class StudentHistoryServiceImpl
        implements StudentHistoryService {

    private final StudentHistoryRepository
            studentHistoryRepository;

    public StudentHistoryServiceImpl(
            StudentHistoryRepository studentHistoryRepository) {

        this.studentHistoryRepository =
                studentHistoryRepository;
    }

    @Override
    public List<StudentHistoryDTO> getStudentHistory(
            String search) {

        String normalizedSearch =
                search != null
                        ? search.trim()
                        : "";

        List<IssueRequestItem> items =
                studentHistoryRepository.findStudentHistory(
                        normalizedSearch
                );

        return items.stream()
                .map(this::convertToDTO)
                .toList();
    }

    private StudentHistoryDTO convertToDTO(
            IssueRequestItem item) {

        StudentHistoryDTO dto =
                new StudentHistoryDTO();

        dto.setItemId(item.getId());

        if (item.getIssueRequest() != null
                && item.getIssueRequest().getStudent() != null) {

            var student =
                    item.getIssueRequest().getStudent();

            dto.setStudentId(student.getId());
            dto.setStudentName(student.getStudentName());
            dto.setRollNumber(student.getRollNumber());

            if (student.getUser() != null) {
                dto.setEmail(
                        student.getUser().getEmail()
                );
            }
        }

        if (item.getBook() != null) {

            dto.setBookId(item.getBook().getId());
            dto.setBookTitle(item.getBook().getTitle());
            dto.setAuthor(item.getBook().getAuthor());
            dto.setIsbn(item.getBook().getIsbn());
        }

        if (item.getIssueRequest() != null) {

            dto.setRequestDate(
                    item.getIssueRequest().getRequestDate()
            );
        }

        dto.setDurationDays(
                item.getDurationDays()
        );

        dto.setStatus(
                item.getStatus()
        );

        dto.setApprovedAt(
                item.getApprovedAt()
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

        dto.setRejectionReason(
                item.getRejectionReason()
        );

        return dto;
    }
}