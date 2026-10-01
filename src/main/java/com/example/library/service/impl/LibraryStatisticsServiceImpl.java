package com.example.library.service.impl;

import com.example.library.dto.LibraryStatisticsDTO;
import com.example.library.entity.Book;
import com.example.library.entity.IssueRequest;
import com.example.library.entity.IssueRequestItem;
import com.example.library.repository.LibraryStatisticsRepository;
import com.example.library.service.LibraryStatisticsService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class LibraryStatisticsServiceImpl
        implements LibraryStatisticsService {

    private static final double FINE_PER_DAY = 5.0;

    private final LibraryStatisticsRepository statisticsRepository;

    public LibraryStatisticsServiceImpl(
            LibraryStatisticsRepository statisticsRepository) {
        this.statisticsRepository = statisticsRepository;
    }

    @Override
    public LibraryStatisticsDTO getLibraryStatistics() {

        LibraryStatisticsDTO statistics = new LibraryStatisticsDTO();

        List<Book> books =
                statisticsRepository.findAllBooksForStatistics();

        List<IssueRequestItem> circulationItems =
                statisticsRepository.findCirculationItems();

        List<IssueRequest> requests =
                statisticsRepository.findAllRequestsForStatistics();

        LocalDateTime now = LocalDateTime.now();
        LocalDate today = LocalDate.now();

        statistics.setTotalBooks(
                statisticsRepository.countTotalBooks()
        );

        statistics.setTotalCopies(
                statisticsRepository.sumTotalCopies()
        );

        statistics.setAvailableCopies(
                statisticsRepository.sumAvailableCopies()
        );

        statistics.setCurrentlyIssued(
                calculateCurrentlyIssued(circulationItems)
        );

        statistics.setOverdueBooks(
                calculateOverdueBooks(circulationItems, today)
        );

        statistics.setTotalStudents(
                statisticsRepository.countTotalStudents()
        );

        statistics.setTotalFineAmount(
                calculateTotalFine(circulationItems, today)
        );

        statistics.setTotalOverdueDays(
                calculateTotalOverdueDays(circulationItems, today)
        );

        statistics.setMonthlyCirculation(
                calculateMonthlyCirculation(circulationItems)
        );

        statistics.setCategoryStatistics(
                calculateCategoryStatistics(
                        books,
                        circulationItems
                )
        );

        statistics.setRequestStatistics(
                calculateRequestStatistics(requests)
        );

        statistics.setActiveStudents(
                calculateActiveStudents(circulationItems)
        );

        statistics.setMonthlyFineStatistics(
                calculateMonthlyFineStatistics(
                        circulationItems,
                        today
                )
        );

        return statistics;
    }

    private long calculateCurrentlyIssued(
            List<IssueRequestItem> items) {

        return items.stream()
                .filter(item ->
                        "ISSUED".equalsIgnoreCase(item.getStatus()))
                .count();
    }

    private long calculateOverdueBooks(
            List<IssueRequestItem> items,
            LocalDate today) {

        return items.stream()
                .filter(this::isCurrentlyIssued)
                .filter(item ->
                        item.getDueDate() != null
                                && item.getDueDate()
                                .toLocalDate()
                                .isBefore(today))
                .count();
    }

    private double calculateTotalFine(
            List<IssueRequestItem> items,
            LocalDate today) {

        double totalFine = 0.0;

        for (IssueRequestItem item : items) {

            long overdueDays =
                    calculateOverdueDays(item, today);

            totalFine += overdueDays * FINE_PER_DAY;
        }

        return totalFine;
    }

    private long calculateTotalOverdueDays(
            List<IssueRequestItem> items,
            LocalDate today) {

        long totalOverdueDays = 0;

        for (IssueRequestItem item : items) {
            totalOverdueDays +=
                    calculateOverdueDays(item, today);
        }

        return totalOverdueDays;
    }

    private long calculateOverdueDays(
            IssueRequestItem item,
            LocalDate today) {

        if (item.getDueDate() == null) {
            return 0;
        }

        LocalDate dueDate =
                item.getDueDate().toLocalDate();

        if ("ISSUED".equalsIgnoreCase(item.getStatus())) {

            if (dueDate.isBefore(today)) {
                return ChronoUnit.DAYS.between(
                        dueDate,
                        today
                );
            }

            return 0;
        }

        if ("RETURNED".equalsIgnoreCase(item.getStatus())
                && item.getReturnedAt() != null) {

            LocalDate returnedDate =
                    item.getReturnedAt().toLocalDate();

            if (returnedDate.isAfter(dueDate)) {
                return ChronoUnit.DAYS.between(
                        dueDate,
                        returnedDate
                );
            }
        }

        return 0;
    }

    private boolean isCurrentlyIssued(
            IssueRequestItem item) {

        return "ISSUED".equalsIgnoreCase(
                item.getStatus()
        ) && item.getReturnedAt() == null;
    }

    private List<LibraryStatisticsDTO.MonthlyCirculationDTO>
    calculateMonthlyCirculation(
            List<IssueRequestItem> items) {

        Map<String, Long> issuedByMonth =
                new HashMap<>();

        Map<String, Long> returnedByMonth =
                new HashMap<>();

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy-MM");

        for (IssueRequestItem item : items) {

            if (item.getIssuedAt() != null) {

                String month =
                        item.getIssuedAt()
                                .format(formatter);

                issuedByMonth.merge(
                        month,
                        1L,
                        Long::sum
                );
            }

            if (item.getReturnedAt() != null) {

                String month =
                        item.getReturnedAt()
                                .format(formatter);

                returnedByMonth.merge(
                        month,
                        1L,
                        Long::sum
                );
            }
        }

        List<String> months =
                new ArrayList<>();

        months.addAll(issuedByMonth.keySet());

        for (String month : returnedByMonth.keySet()) {
            if (!months.contains(month)) {
                months.add(month);
            }
        }

        months.sort(Comparator.naturalOrder());

        List<LibraryStatisticsDTO.MonthlyCirculationDTO>
                result = new ArrayList<>();

        for (String month : months) {

            LibraryStatisticsDTO.MonthlyCirculationDTO
                    dto =
                    new LibraryStatisticsDTO
                            .MonthlyCirculationDTO();

            dto.setMonth(month);

            dto.setIssued(
                    issuedByMonth.getOrDefault(
                            month,
                            0L
                    )
            );

            dto.setReturned(
                    returnedByMonth.getOrDefault(
                            month,
                            0L
                    )
            );

            result.add(dto);
        }

        return result;
    }

    private List<LibraryStatisticsDTO.CategoryStatisticsDTO>
    calculateCategoryStatistics(
            List<Book> books,
            List<IssueRequestItem> circulationItems) {

        Map<String, Long> totalCopies =
                new LinkedHashMap<>();

        Map<String, Long> availableCopies =
                new LinkedHashMap<>();

        Map<String, Long> issuedCopies =
                new LinkedHashMap<>();

        for (Book book : books) {

            String category =
                    normalizeCategory(book.getCategory());

            totalCopies.merge(
                    category,
                    (long) book.getTotalCopies(),
                    Long::sum
            );

            availableCopies.merge(
                    category,
                    (long) book.getAvailableCopies(),
                    Long::sum
            );
        }

        for (IssueRequestItem item : circulationItems) {

            if (!"ISSUED".equalsIgnoreCase(
                    item.getStatus())) {
                continue;
            }

            if (item.getBook() == null) {
                continue;
            }

            String category =
                    normalizeCategory(
                            item.getBook().getCategory()
                    );

            issuedCopies.merge(
                    category,
                    1L,
                    Long::sum
            );
        }

        List<String> categories =
                new ArrayList<>(totalCopies.keySet());

        categories.sort(Comparator.naturalOrder());

        List<LibraryStatisticsDTO.CategoryStatisticsDTO>
                result = new ArrayList<>();

        for (String category : categories) {

            LibraryStatisticsDTO.CategoryStatisticsDTO
                    dto =
                    new LibraryStatisticsDTO
                            .CategoryStatisticsDTO();

            dto.setCategory(category);

            dto.setTotalCopies(
                    totalCopies.getOrDefault(
                            category,
                            0L
                    )
            );

            dto.setAvailableCopies(
                    availableCopies.getOrDefault(
                            category,
                            0L
                    )
            );

            dto.setIssuedCopies(
                    issuedCopies.getOrDefault(
                            category,
                            0L
                    )
            );

            result.add(dto);
        }

        return result;
    }

    private String normalizeCategory(String category) {

        if (category == null
                || category.isBlank()) {
            return "Uncategorized";
        }

        return category.trim();
    }

    private List<LibraryStatisticsDTO.RequestStatisticsDTO>
    calculateRequestStatistics(
            List<IssueRequest> requests) {

        Map<String, Long> requestCounts =
                new LinkedHashMap<>();

        for (IssueRequest request : requests) {

            String status = request.getStatus();

            if (status == null
                    || status.isBlank()) {
                status = "UNKNOWN";
            }

            requestCounts.merge(
                    status,
                    1L,
                    Long::sum
            );
        }

        List<LibraryStatisticsDTO.RequestStatisticsDTO>
                result = new ArrayList<>();

        requestCounts.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {

                    LibraryStatisticsDTO
                            .RequestStatisticsDTO dto =
                            new LibraryStatisticsDTO
                                    .RequestStatisticsDTO();

                    dto.setStatus(entry.getKey());
                    dto.setCount(entry.getValue());

                    result.add(dto);
                });

        return result;
    }

    private List<LibraryStatisticsDTO.ActiveStudentStatisticsDTO>
    calculateActiveStudents(
            List<IssueRequestItem> items) {

        Map<Long, StudentActivity> studentMap =
                new HashMap<>();

        for (IssueRequestItem item : items) {

            if (item.getIssueRequest() == null
                    || item.getIssueRequest().getStudent() == null) {
                continue;
            }

            Long studentId =
                    item.getIssueRequest()
                            .getStudent()
                            .getId();

            StudentActivity activity =
                    studentMap.computeIfAbsent(
                            studentId,
                            id -> new StudentActivity()
                    );

            activity.studentName =
                    item.getIssueRequest()
                            .getStudent()
                            .getStudentName();

            activity.rollNumber =
                    item.getIssueRequest()
                            .getStudent()
                            .getRollNumber();

            if ("ISSUED".equalsIgnoreCase(
                    item.getStatus())) {

                activity.issuedCount++;

                if (item.getReturnedAt() == null) {
                    activity.currentlyIssued++;
                }

            } else if ("RETURNED".equalsIgnoreCase(
                    item.getStatus())) {

                activity.returnedCount++;
            }
        }

        List<LibraryStatisticsDTO.ActiveStudentStatisticsDTO>
                result = new ArrayList<>();

        for (StudentActivity activity :
                studentMap.values()) {

            if (activity.currentlyIssued <= 0) {
                continue;
            }

            LibraryStatisticsDTO
                    .ActiveStudentStatisticsDTO dto =
                    new LibraryStatisticsDTO
                            .ActiveStudentStatisticsDTO();

            dto.setStudentName(
                    activity.studentName
            );

            dto.setRollNumber(
                    activity.rollNumber
            );

            dto.setIssuedCount(
                    activity.issuedCount
            );

            dto.setReturnedCount(
                    activity.returnedCount
            );

            dto.setCurrentlyIssued(
                    activity.currentlyIssued
            );

            result.add(dto);
        }

        result.sort(
                Comparator.comparing(
                        LibraryStatisticsDTO
                                .ActiveStudentStatisticsDTO
                                ::getCurrentlyIssued
                ).reversed()
        );

        return result;
    }

    private List<LibraryStatisticsDTO.MonthlyFineStatisticsDTO>
    calculateMonthlyFineStatistics(
            List<IssueRequestItem> items,
            LocalDate today) {

        Map<String, FineActivity> monthlyFine =
                new HashMap<>();

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy-MM");

        for (IssueRequestItem item : items) {

            long overdueDays =
                    calculateOverdueDays(
                            item,
                            today
                    );

            if (overdueDays <= 0) {
                continue;
            }

            LocalDateTime referenceDate =
                    item.getReturnedAt() != null
                            ? item.getReturnedAt()
                            : item.getDueDate();

            if (referenceDate == null) {
                continue;
            }

            String month =
                    referenceDate.format(formatter);

            FineActivity activity =
                    monthlyFine.computeIfAbsent(
                            month,
                            key -> new FineActivity()
                    );

            activity.fineAmount +=
                    overdueDays * FINE_PER_DAY;

            activity.overdueRecords++;

            activity.overdueDays +=
                    overdueDays;
        }

        List<String> months =
                new ArrayList<>(
                        monthlyFine.keySet()
                );

        months.sort(Comparator.naturalOrder());

        List<LibraryStatisticsDTO.MonthlyFineStatisticsDTO>
                result = new ArrayList<>();

        for (String month : months) {

            FineActivity activity =
                    monthlyFine.get(month);

            LibraryStatisticsDTO
                    .MonthlyFineStatisticsDTO dto =
                    new LibraryStatisticsDTO
                            .MonthlyFineStatisticsDTO();

            dto.setMonth(month);

            dto.setFineAmount(
                    activity.fineAmount
            );

            dto.setOverdueRecords(
                    activity.overdueRecords
            );

            dto.setOverdueDays(
                    activity.overdueDays
            );

            result.add(dto);
        }

        return result;
    }

    private static class StudentActivity {

        private String studentName;
        private String rollNumber;
        private long issuedCount;
        private long returnedCount;
        private long currentlyIssued;
    }

    private static class FineActivity {

        private double fineAmount;
        private long overdueRecords;
        private long overdueDays;
    }
}