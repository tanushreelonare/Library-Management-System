package com.example.library.service.impl;

import com.example.library.dto.DonationRequestDTO;
import com.example.library.entity.Book;
import com.example.library.entity.DonationRequest;
import com.example.library.entity.Student;
import com.example.library.repository.BookRepository;
import com.example.library.repository.DonationRequestRepository;
import com.example.library.repository.StudentRepository;
import com.example.library.service.DonationRequestService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;

@Service
@Transactional
public class DonationRequestServiceImpl implements DonationRequestService {

    private final DonationRequestRepository donationRequestRepository;
    private final StudentRepository studentRepository;
    private final BookRepository bookRepository;

    public DonationRequestServiceImpl(
            DonationRequestRepository donationRequestRepository,
            StudentRepository studentRepository,
            BookRepository bookRepository) {

        this.donationRequestRepository = donationRequestRepository;
        this.studentRepository = studentRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public DonationRequestDTO submitDonationRequest(
            Long studentId,
            DonationRequestDTO dto) {

        Student student = studentRepository
                .findById(studentId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Student not found.")
                );

        validateDonationRequest(dto);

        DonationRequest request = new DonationRequest();

        request.setStudent(student);
        request.setBookName(dto.getBookName().trim());
        request.setAuthor(dto.getAuthor().trim());
        request.setCategory(dto.getCategory().trim());
        request.setTotalCopies(dto.getTotalCopies());
        request.setStatus("PENDING");
        request.setRequestDate(LocalDateTime.now());

        DonationRequest saved =
                donationRequestRepository.save(request);

        return mapToDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DonationRequestDTO> getStudentDonationRequests(
            Long studentId) {

        return donationRequestRepository
                .findByStudentId(studentId)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DonationRequestDTO> getAllDonationRequests() {

        return donationRequestRepository
                .findAllForLibrarian()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DonationRequestDTO> getPendingDonationRequests() {

        return donationRequestRepository
                .findPendingRequests()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DonationRequestDTO> searchDonationRequests(
            String search) {

        String normalizedSearch =
                search == null ? "" : search.trim();

        return donationRequestRepository
                .searchForLibrarian(normalizedSearch)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DonationRequestDTO> getDonationRequestsByStatus(
            String status) {

        if (status == null || status.isBlank()) {
            return getAllDonationRequests();
        }

        return donationRequestRepository
                .findByStatus(status.trim().toUpperCase())
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public DonationRequestDTO approveDonationRequest(
            Long requestId) {

        DonationRequest request =
                donationRequestRepository
                        .findById(requestId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Donation request not found."
                                )
                        );

        if (!"PENDING".equalsIgnoreCase(
                request.getStatus())) {

            throw new IllegalStateException(
                    "Only pending donation requests can be approved."
            );
        }

        Book book = new Book();

        book.setIsbn(generateDonationIsbn());

        book.setTitle(
                request.getBookName().trim()
        );

        book.setAuthor(
                request.getAuthor().trim()
        );

        book.setCategory(
                request.getCategory().trim()
        );

        book.setPublisher(
                "Donated Book"
        );

        book.setPublicationYear(
                Year.now().getValue()
        );

        book.setLanguage(
                "Not Specified"
        );

        book.setTotalCopies(
                request.getTotalCopies()
        );

        book.setAvailableCopies(
                request.getTotalCopies()
        );

        book.setDescription(
                "Book donated by a library student."
        );

        book.setActive(true);

        bookRepository.save(book);

        request.setStatus("APPROVED");
        request.setReviewedAt(LocalDateTime.now());
        request.setRejectionReason(null);

        DonationRequest saved =
                donationRequestRepository.save(request);

        return mapToDTO(saved);
    }

    @Override
    public DonationRequestDTO rejectDonationRequest(
            Long requestId,
            String rejectionReason) {

        DonationRequest request =
                donationRequestRepository
                        .findById(requestId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Donation request not found."
                                )
                        );

        if (!"PENDING".equalsIgnoreCase(
                request.getStatus())) {

            throw new IllegalStateException(
                    "Only pending donation requests can be rejected."
            );
        }

        request.setStatus("REJECTED");
        request.setReviewedAt(LocalDateTime.now());

        // Rejection reason is optional.
        request.setRejectionReason(
                rejectionReason == null || rejectionReason.isBlank()
                        ? null
                        : rejectionReason.trim()
        );

        DonationRequest saved =
                donationRequestRepository.save(request);

        return mapToDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public DonationRequestDTO getDonationRequestById(
            Long requestId) {

        DonationRequest request =
                donationRequestRepository
                        .findById(requestId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Donation request not found."
                                )
                        );

        return mapToDTO(request);
    }

    // Generate a unique ISBN for an approved donation
    private String generateDonationIsbn() {

        String isbn;

        do {
            long timestamp =
                    System.currentTimeMillis() % 100000000L;

            isbn =
                    "DON"
                            + Year.now().getValue()
                            + String.format(
                                    "%08d",
                                    timestamp
                            );

        } while (
                bookRepository.existsByIsbn(isbn)
        );

        return isbn;
    }

    private void validateDonationRequest(
            DonationRequestDTO dto) {

        if (dto == null) {

            throw new IllegalArgumentException(
                    "Donation request cannot be empty."
            );
        }

        if (dto.getBookName() == null ||
                dto.getBookName().isBlank()) {

            throw new IllegalArgumentException(
                    "Book name is required."
            );
        }

        if (dto.getAuthor() == null ||
                dto.getAuthor().isBlank()) {

            throw new IllegalArgumentException(
                    "Author is required."
            );
        }

        if (dto.getCategory() == null ||
                dto.getCategory().isBlank()) {

            throw new IllegalArgumentException(
                    "Category is required."
            );
        }

        if (dto.getTotalCopies() == null ||
                dto.getTotalCopies() <= 0) {

            throw new IllegalArgumentException(
                    "Total copies must be greater than zero."
            );
        }
    }

    private DonationRequestDTO mapToDTO(
            DonationRequest request) {

        DonationRequestDTO dto =
                new DonationRequestDTO();

        dto.setRequestId(request.getId());

        if (request.getStudent() != null) {

            Student student =
                    request.getStudent();

            dto.setStudentId(student.getId());

            dto.setStudentName(
                    student.getStudentName()
            );

            dto.setRollNumber(
                    student.getRollNumber()
            );

            if (student.getUser() != null) {

                dto.setEmail(
                        student.getUser().getEmail()
                );
            }
        }

        dto.setBookName(
                request.getBookName()
        );

        dto.setAuthor(
                request.getAuthor()
        );

        dto.setCategory(
                request.getCategory()
        );

        dto.setTotalCopies(
                request.getTotalCopies()
        );

        dto.setStatus(
                request.getStatus()
        );

        dto.setRequestDate(
                request.getRequestDate()
        );

        dto.setReviewedAt(
                request.getReviewedAt()
        );

        dto.setRejectionReason(
                request.getRejectionReason()
        );

        return dto;
    }
}