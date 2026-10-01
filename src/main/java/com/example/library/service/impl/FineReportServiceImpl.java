package com.example.library.service.impl;

import com.example.library.dto.FineReportDTO;
import com.example.library.entity.Book;
import com.example.library.entity.IssueRequest;
import com.example.library.entity.IssueRequestItem;
import com.example.library.entity.Student;
import com.example.library.repository.FineReportRepository;
import com.example.library.service.FineReportService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class FineReportServiceImpl implements FineReportService {

    private static final double FINE_RATE_PER_DAY = 5.0;

    private final FineReportRepository fineReportRepository;

    public FineReportServiceImpl(
            FineReportRepository fineReportRepository) {

        this.fineReportRepository = fineReportRepository;
    }

    @Override
    public List<FineReportDTO> getFineReports(
            String search,
            String status) {

        return fineReportRepository.findFineReportItems()
                .stream()
                .map(this::convertToDTO)
                .filter(dto -> dto != null)
                .filter(dto -> matchesSearch(dto, search))
                .filter(dto -> matchesStatus(dto, status))
                .toList();
    }

    private FineReportDTO convertToDTO(IssueRequestItem item) {

        if (item == null || item.getDueDate() == null) {
            return null;
        }

        LocalDate dueDate = item.getDueDate().toLocalDate();

        LocalDate effectiveDate;

        if (item.getReturnedAt() != null) {
            effectiveDate = item.getReturnedAt().toLocalDate();
        } else {
            effectiveDate = LocalDate.now();
        }

        long overdueDays = ChronoUnit.DAYS.between(
                dueDate,
                effectiveDate
        );

        if (overdueDays <= 0) {
            return null;
        }

        FineReportDTO dto = new FineReportDTO();

        dto.setItemId(item.getId());

        Book book = item.getBook();

        if (book != null) {
            dto.setBookId(book.getId());
            dto.setBookTitle(book.getTitle());
            dto.setIsbn(book.getIsbn());
        }

        IssueRequest request = item.getIssueRequest();

        if (request != null) {

            Student student = request.getStudent();

            if (student != null) {
                dto.setStudentName(student.getStudentName());
                dto.setRollNumber(student.getRollNumber());
            }
        }

        dto.setIssuedAt(item.getIssuedAt());
        dto.setDueDate(item.getDueDate());
        dto.setReturnedAt(item.getReturnedAt());

        dto.setOverdueDays(overdueDays);

        dto.setFineAmount(
                overdueDays * FINE_RATE_PER_DAY
        );

        if (item.getReturnedAt() != null) {
            dto.setStatus("RETURNED LATE");
        } else {
            dto.setStatus("OVERDUE");
        }

        return dto;
    }

    private boolean matchesSearch(
            FineReportDTO dto,
            String search) {

        if (search == null || search.isBlank()) {
            return true;
        }

        String keyword = search.trim().toLowerCase();

        return contains(dto.getStudentName(), keyword)
                || contains(dto.getRollNumber(), keyword)
                || contains(dto.getBookTitle(), keyword)
                || contains(dto.getIsbn(), keyword);
    }

    private boolean matchesStatus(
            FineReportDTO dto,
            String status) {

        if (status == null || status.isBlank()
                || "ALL".equalsIgnoreCase(status)) {
            return true;
        }

        return dto.getStatus() != null
                && dto.getStatus().equalsIgnoreCase(
                        status.trim()
                );
    }

    private boolean contains(
            String value,
            String keyword) {

        return value != null
                && value.toLowerCase().contains(keyword);
    }
}