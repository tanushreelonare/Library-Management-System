package com.example.library.service.impl;

import com.example.library.dto.MyRequestDTO;
import com.example.library.dto.MyRequestDTO.RequestItemDTO;
import com.example.library.entity.Book;
import com.example.library.entity.IssueRequest;
import com.example.library.entity.IssueRequestItem;
import com.example.library.entity.Student;
import com.example.library.entity.User;
import com.example.library.repository.BookRepository;
import com.example.library.repository.IssueRequestItemRepository;
import com.example.library.repository.IssueRequestRepository;
import com.example.library.repository.StudentRepository;
import com.example.library.repository.UserRepository;
import com.example.library.service.IssueRequestService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class IssueRequestServiceImpl implements IssueRequestService {

    private static final int MAX_BOOKS = 6;

    private static final String NEW_ISSUE = "NEW_ISSUE";
    private static final String REISSUE = "REISSUE";

    private static final String PENDING = "PENDING";
    private static final String ISSUED = "ISSUED";
    private static final String REJECTED = "REJECTED";
    private static final String APPROVED = "APPROVED";
    private static final String REISSUE_APPROVED = "REISSUE_APPROVED";

    private static final Set<Integer> ALLOWED_DURATIONS =
            Set.of(7, 14, 21, 30);

    private final IssueRequestRepository issueRequestRepository;
    private final IssueRequestItemRepository issueRequestItemRepository;
    private final BookRepository bookRepository;
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;

    public IssueRequestServiceImpl(
            IssueRequestRepository issueRequestRepository,
            IssueRequestItemRepository issueRequestItemRepository,
            BookRepository bookRepository,
            StudentRepository studentRepository,
            UserRepository userRepository) {

        this.issueRequestRepository = issueRequestRepository;
        this.issueRequestItemRepository = issueRequestItemRepository;
        this.bookRepository = bookRepository;
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
    }

    // Student Requests

    @Override
    @Transactional(readOnly = true)
    public List<MyRequestDTO> getStudentRequests(
            String email,
            String search,
            String status) {

        List<IssueRequest> requests =
                issueRequestRepository.findByStudentEmail(email);

        return requests.stream()
                .filter(request -> filterRequests(
                        request,
                        search,
                        status
                ))
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public MyRequestDTO getStudentRequestById(
            Long requestId,
            String email) {

        IssueRequest request =
                issueRequestRepository
                        .findByIdAndStudentEmail(requestId, email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Issue request not found"
                                ));

        return convertToDTO(request);
    }

    @Override
    public void cancelRequest(
            Long requestId,
            String email) {

        IssueRequest request =
                issueRequestRepository
                        .findByIdAndStudentEmail(requestId, email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Issue request not found"
                                ));

        if (!PENDING.equals(request.getStatus())) {
            throw new RuntimeException(
                    "Only pending requests can be cancelled"
            );
        }

        for (IssueRequestItem item : request.getItems()) {

            if (PENDING.equals(item.getStatus())) {
                item.setStatus("CANCELLED");
            }
        }

        request.setStatus("CANCELLED");

        issueRequestRepository.save(request);
    }

    // Submit normal issue request

    @Override
    public void submitIssueRequest(
            String email,
            Map<Long, Integer> bookDurations) {

        if (bookDurations == null || bookDurations.isEmpty()) {
            throw new RuntimeException(
                    "Please select at least one book"
            );
        }

        if (bookDurations.size() > MAX_BOOKS) {
            throw new RuntimeException(
                    "You can request a maximum of "
                            + MAX_BOOKS
                            + " books"
            );
        }

        User user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"
                                ));

        Student student =
                studentRepository
                        .findByUserId(user.getId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Student profile not found"
                                ));

        long activeBookRequests =
                issueRequestRepository.countActiveBookRequests(
                        student.getId()
                );

        if (activeBookRequests + bookDurations.size()
                > MAX_BOOKS) {

            throw new RuntimeException(
                    "You can have a maximum of "
                            + MAX_BOOKS
                            + " active books"
            );
        }

        IssueRequest request = new IssueRequest();

        request.setStudent(student);
        request.setRequestDate(LocalDateTime.now());
        request.setStatus(PENDING);
        request.setRequestType(NEW_ISSUE);

        List<IssueRequestItem> items = new ArrayList<>();

        for (Map.Entry<Long, Integer> entry
                : bookDurations.entrySet()) {

            Long bookId = entry.getKey();
            Integer durationDays = entry.getValue();

            if (durationDays == null
                    || !ALLOWED_DURATIONS.contains(durationDays)) {

                throw new RuntimeException(
                        "Invalid duration selected"
                );
            }

            Book book =
                    bookRepository
                            .findById(bookId)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Book not found"
                                    ));

            if (!book.isActive()) {
                throw new RuntimeException(
                        "Book is not active: "
                                + book.getTitle()
                );
            }

            if (book.getAvailableCopies() <= 0) {
                throw new RuntimeException(
                        "Book is currently unavailable: "
                                + book.getTitle()
                );
            }

            boolean alreadyRequested =
                    issueRequestRepository
                            .existsPendingRequestForBook(
                                    student.getId(),
                                    bookId
                            );

            if (alreadyRequested) {
                throw new RuntimeException(
                        "You already have a pending request for: "
                                + book.getTitle()
                );
            }

            IssueRequestItem item =
                    new IssueRequestItem();

            item.setIssueRequest(request);
            item.setBook(book);
            item.setDurationDays(durationDays);
            item.setStatus(PENDING);

            items.add(item);
        }

        request.setItems(items);

        issueRequestRepository.save(request);
    }

    // Submit re-issue request

    @Override
    public void submitReissueRequest(
            Long itemId,
            Integer durationDays,
            String email) {

        if (itemId == null) {
            throw new RuntimeException(
                    "Issued book information is missing"
            );
        }

        if (durationDays == null
                || !ALLOWED_DURATIONS.contains(durationDays)) {

            throw new RuntimeException(
                    "Invalid re-issue duration selected"
            );
        }

        User user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"
                                ));

        Student student =
                studentRepository
                        .findByUserId(user.getId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Student profile not found"
                                ));

        IssueRequestItem originalItem =
                issueRequestItemRepository
                        .findById(itemId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Issued book record not found"
                                ));

        IssueRequest originalRequest =
                originalItem.getIssueRequest();

        if (originalRequest == null
                || originalRequest.getStudent() == null
                || originalRequest.getStudent().getId() == null
                || !originalRequest.getStudent()
                        .getId()
                        .equals(student.getId())) {

            throw new RuntimeException(
                    "You are not authorized to request a re-issue for this book"
            );
        }

        if (!ISSUED.equals(originalItem.getStatus())) {
            throw new RuntimeException(
                    "Only currently issued books can be re-issued"
            );
        }

        if (originalItem.getDueDate() == null) {
            throw new RuntimeException(
                    "Current due date is not available"
            );
        }

        boolean pendingReissue =
                issueRequestRepository
                        .existsPendingReissueRequest(itemId);

        if (pendingReissue) {
            throw new RuntimeException(
                    "A re-issue request is already pending for this book"
            );
        }

        Book book = originalItem.getBook();

        if (book == null) {
            throw new RuntimeException(
                    "Book not found for issued record"
            );
        }

        IssueRequest reissueRequest =
                new IssueRequest();

        reissueRequest.setStudent(student);
        reissueRequest.setRequestDate(LocalDateTime.now());
        reissueRequest.setStatus(PENDING);
        reissueRequest.setRequestType(REISSUE);

        IssueRequestItem reissueItem =
                new IssueRequestItem();

        reissueItem.setIssueRequest(reissueRequest);
        reissueItem.setBook(book);
        reissueItem.setDurationDays(durationDays);
        reissueItem.setStatus(PENDING);
        reissueItem.setReissueOfItem(originalItem);

        List<IssueRequestItem> items =
                new ArrayList<>();

        items.add(reissueItem);

        reissueRequest.setItems(items);

        issueRequestRepository.save(reissueRequest);
    }

    // Librarian Requests

    @Override
    @Transactional(readOnly = true)
    public List<MyRequestDTO> getAllLibrarianRequests(
            String search,
            String status) {

        List<IssueRequest> requests =
                issueRequestRepository
                        .findAllRequestsForLibrarian();

        return requests.stream()
                .filter(request -> filterRequests(
                        request,
                        search,
                        status
                ))
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public MyRequestDTO getLibrarianRequestById(
            Long requestId) {

        IssueRequest request =
                issueRequestRepository
                        .findRequestForLibrarian(requestId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Issue request not found"
                                ));

        return convertToDTO(request);
    }

    // Approve entire normal issue request

    @Override
    public void approveRequest(Long requestId) {

        IssueRequest request =
                issueRequestRepository
                        .findRequestForLibrarian(requestId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Issue request not found"
                                ));

        ensureNormalIssueRequest(request);

        if (!PENDING.equals(request.getStatus())
                && !"PARTIALLY_APPROVED".equals(
                        request.getStatus())) {

            throw new RuntimeException(
                    "This request cannot be approved"
            );
        }

        List<Long> pendingItemIds =
                request.getItems()
                        .stream()
                        .filter(item ->
                                PENDING.equals(item.getStatus()))
                        .map(IssueRequestItem::getId)
                        .toList();

        if (pendingItemIds.isEmpty()) {
            throw new RuntimeException(
                    "There are no pending books to approve"
            );
        }

        approveRequestItems(
                requestId,
                pendingItemIds
        );
    }

    // Reject entire normal issue request

    @Override
    public void rejectRequest(
            Long requestId,
            String rejectionReason) {

        IssueRequest request =
                issueRequestRepository
                        .findRequestForLibrarian(requestId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Issue request not found"
                                ));

        ensureNormalIssueRequest(request);

        if (!PENDING.equals(request.getStatus())
                && !"PARTIALLY_APPROVED".equals(
                        request.getStatus())) {

            throw new RuntimeException(
                    "This request cannot be rejected"
            );
        }

        List<Long> pendingItemIds =
                request.getItems()
                        .stream()
                        .filter(item ->
                                PENDING.equals(item.getStatus()))
                        .map(IssueRequestItem::getId)
                        .toList();

        if (pendingItemIds.isEmpty()) {
            throw new RuntimeException(
                    "There are no pending books to reject"
            );
        }

        rejectRequestItems(
                requestId,
                pendingItemIds,
                rejectionReason
        );
    }

    // Approve selected books from normal issue request

    @Override
    public void approveRequestItems(
            Long requestId,
            List<Long> itemIds) {

        if (itemIds == null || itemIds.isEmpty()) {
            throw new RuntimeException(
                    "Please select at least one book"
            );
        }

        IssueRequest request =
                issueRequestRepository
                        .findRequestForLibrarian(requestId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Issue request not found"
                                ));

        ensureNormalIssueRequest(request);

        if (!PENDING.equals(request.getStatus())
                && !"PARTIALLY_APPROVED".equals(
                        request.getStatus())) {

            throw new RuntimeException(
                    "This request cannot be approved"
            );
        }

        Set<Long> requestItemIds =
                request.getItems()
                        .stream()
                        .map(IssueRequestItem::getId)
                        .collect(Collectors.toSet());

        for (Long itemId : itemIds) {

            if (!requestItemIds.contains(itemId)) {
                throw new RuntimeException(
                        "Invalid book selected"
                );
            }
        }

        LocalDateTime now =
                LocalDateTime.now();

        for (IssueRequestItem item :
                request.getItems()) {

            if (!itemIds.contains(item.getId())) {
                continue;
            }

            if (!PENDING.equals(item.getStatus())) {
                throw new RuntimeException(
                        "Only pending books can be approved"
                );
            }

            Book book = item.getBook();

            if (book == null) {
                throw new RuntimeException(
                        "Book not found for request item"
                );
            }

            if (!book.isActive()) {
                throw new RuntimeException(
                        "Book is not active: "
                                + book.getTitle()
                );
            }

            if (book.getAvailableCopies() <= 0) {
                throw new RuntimeException(
                        "Book is no longer available: "
                                + book.getTitle()
                );
            }

            // Decrease available copies because the book is issued.

            book.setAvailableCopies(
                    book.getAvailableCopies() - 1
            );

            bookRepository.save(book);

            // Mark the book as issued.

            item.setStatus(ISSUED);

            item.setApprovedAt(now);

            item.setIssuedAt(now);

            if (item.getDurationDays() != null) {

                item.setDueDate(
                        now.plusDays(
                                item.getDurationDays()
                        )
                );
            }

            item.setRejectionReason(null);

            issueRequestItemRepository.save(item);
        }

        updateRequestStatus(request);
    }

    // Reject selected books from normal issue request

    @Override
    public void rejectRequestItems(
            Long requestId,
            List<Long> itemIds,
            String rejectionReason) {

        if (itemIds == null || itemIds.isEmpty()) {
            throw new RuntimeException(
                    "Please select at least one book"
            );
        }

        if (rejectionReason != null
                && rejectionReason.length() > 500) {

            throw new RuntimeException(
                    "Rejection reason cannot exceed 500 characters"
            );
        }

        IssueRequest request =
                issueRequestRepository
                        .findRequestForLibrarian(requestId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Issue request not found"
                                ));

        ensureNormalIssueRequest(request);

        if (!PENDING.equals(request.getStatus())
                && !"PARTIALLY_APPROVED".equals(
                        request.getStatus())) {

            throw new RuntimeException(
                    "This request cannot be rejected"
            );
        }

        Set<Long> requestItemIds =
                request.getItems()
                        .stream()
                        .map(IssueRequestItem::getId)
                        .collect(Collectors.toSet());

        for (Long itemId : itemIds) {

            if (!requestItemIds.contains(itemId)) {
                throw new RuntimeException(
                        "Invalid book selected"
                );
            }
        }

        for (IssueRequestItem item :
                request.getItems()) {

            if (!itemIds.contains(item.getId())) {
                continue;
            }

            if (!PENDING.equals(item.getStatus())) {
                throw new RuntimeException(
                        "Only pending books can be rejected"
                );
            }

            item.setStatus(REJECTED);

            if (rejectionReason != null
                    && !rejectionReason.isBlank()) {

                item.setRejectionReason(
                        rejectionReason.trim()
                );

            } else {

                item.setRejectionReason(null);
            }

            issueRequestItemRepository.save(item);
        }

        updateRequestStatus(request);
    }

    // Approve re-issue request

    @Override
    public void approveReissueRequest(
            Long requestId) {

        IssueRequest request =
                issueRequestRepository
                        .findRequestForLibrarian(requestId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Issue request not found"
                                ));

        ensureReissueRequest(request);

        if (!PENDING.equals(request.getStatus())) {
            throw new RuntimeException(
                    "This re-issue request is no longer pending"
            );
        }

        if (request.getItems() == null
                || request.getItems().size() != 1) {

            throw new RuntimeException(
                    "Invalid re-issue request"
            );
        }

        IssueRequestItem reissueItem =
                request.getItems().get(0);

        if (!PENDING.equals(reissueItem.getStatus())) {
            throw new RuntimeException(
                    "This re-issue request has already been processed"
            );
        }

        IssueRequestItem originalItem =
                getOriginalIssuedItem(reissueItem);

        if (!ISSUED.equals(originalItem.getStatus())) {
            throw new RuntimeException(
                    "The original book is no longer issued"
            );
        }

        if (originalItem.getDueDate() == null) {
            throw new RuntimeException(
                    "The original book does not have a due date"
            );
        }

        if (reissueItem.getDurationDays() == null
                || !ALLOWED_DURATIONS.contains(
                        reissueItem.getDurationDays())) {

            throw new RuntimeException(
                    "Invalid re-issue duration"
            );
        }

        LocalDateTime now =
                LocalDateTime.now();

        LocalDateTime newDueDate =
                originalItem
                        .getDueDate()
                        .plusDays(
                                reissueItem.getDurationDays()
                        );

        // Extend the original issued item's due date.

        originalItem.setDueDate(newDueDate);

        issueRequestItemRepository.save(originalItem);

        // Record the re-issue approval.

        reissueItem.setStatus(REISSUE_APPROVED);
        reissueItem.setApprovedAt(now);
        reissueItem.setDueDate(newDueDate);
        reissueItem.setRejectionReason(null);

        issueRequestItemRepository.save(reissueItem);

        request.setStatus(APPROVED);

        issueRequestRepository.save(request);
    }

    // Reject re-issue request

    @Override
    public void rejectReissueRequest(
            Long requestId,
            String rejectionReason) {

        IssueRequest request =
                issueRequestRepository
                        .findRequestForLibrarian(requestId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Issue request not found"
                                ));

        ensureReissueRequest(request);

        if (!PENDING.equals(request.getStatus())) {
            throw new RuntimeException(
                    "This re-issue request is no longer pending"
            );
        }

        if (rejectionReason != null
                && rejectionReason.length() > 500) {

            throw new RuntimeException(
                    "Rejection reason cannot exceed 500 characters"
            );
        }

        if (request.getItems() == null
                || request.getItems().size() != 1) {

            throw new RuntimeException(
                    "Invalid re-issue request"
            );
        }

        IssueRequestItem reissueItem =
                request.getItems().get(0);

        if (!PENDING.equals(reissueItem.getStatus())) {
            throw new RuntimeException(
                    "This re-issue request has already been processed"
            );
        }

        // The original issued item is intentionally unchanged.

        reissueItem.setStatus(REJECTED);

        if (rejectionReason != null
                && !rejectionReason.isBlank()) {

            reissueItem.setRejectionReason(
                    rejectionReason.trim()
            );

        } else {

            reissueItem.setRejectionReason(null);
        }

        issueRequestItemRepository.save(reissueItem);

        request.setStatus(REJECTED);

        issueRequestRepository.save(request);
    }

    // Get original issued item for a re-issue request

    private IssueRequestItem getOriginalIssuedItem(
            IssueRequestItem reissueItem) {

        if (reissueItem.getReissueOfItem() == null
                || reissueItem.getReissueOfItem().getId() == null) {

            throw new RuntimeException(
                    "Original issued book record not found"
            );
        }

        Long originalItemId =
                reissueItem
                        .getReissueOfItem()
                        .getId();

        return issueRequestItemRepository
                .findById(originalItemId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Original issued book record not found"
                        ));
    }

    // Ensure a request uses the normal issue workflow

    private void ensureNormalIssueRequest(
            IssueRequest request) {

        if (!NEW_ISSUE.equals(request.getRequestType())) {
            throw new RuntimeException(
                    "This is a re-issue request. "
                            + "Use the re-issue approval workflow."
            );
        }
    }

    // Ensure a request uses the re-issue workflow

    private void ensureReissueRequest(
            IssueRequest request) {

        if (!REISSUE.equals(request.getRequestType())) {
            throw new RuntimeException(
                    "This is a normal book issue request."
            );
        }
    }

    // Update normal request-level status

    private void updateRequestStatus(
            IssueRequest request) {

        List<IssueRequestItem> items =
                request.getItems();

        if (items == null || items.isEmpty()) {

            request.setStatus(PENDING);

            issueRequestRepository.save(request);

            return;
        }

        boolean allIssued =
                items.stream()
                        .allMatch(item ->
                                ISSUED.equals(
                                        item.getStatus()
                                ));

        boolean allRejected =
                items.stream()
                        .allMatch(item ->
                                REJECTED.equals(
                                        item.getStatus()
                                ));

        boolean anyIssued =
                items.stream()
                        .anyMatch(item ->
                                ISSUED.equals(
                                        item.getStatus()
                                ));

        boolean anyPending =
                items.stream()
                        .anyMatch(item ->
                                PENDING.equals(
                                        item.getStatus()
                                ));

        if (allIssued) {

            request.setStatus(APPROVED);

        } else if (allRejected) {

            request.setStatus(REJECTED);

        } else if (anyIssued
                && (anyPending
                || items.stream()
                        .anyMatch(item ->
                                REJECTED.equals(
                                        item.getStatus()
                                )))) {

            request.setStatus(
                    "PARTIALLY_APPROVED"
            );

        } else {

            request.setStatus(PENDING);
        }

        issueRequestRepository.save(request);
    }

    // Convert request to DTO

    private MyRequestDTO convertToDTO(
            IssueRequest request) {

        MyRequestDTO dto =
                new MyRequestDTO();

        dto.setRequestId(
                request.getId()
        );

        dto.setRequestDate(
                request.getRequestDate()
        );

        dto.setStatus(
                request.getStatus()
        );

        dto.setRequestType(
                request.getRequestType()
        );

        if (request.getStudent() != null) {

            dto.setStudentName(
                    request.getStudent().getStudentName()
            );

            dto.setRollNumber(
                    request.getStudent().getRollNumber()
            );
        }

        List<RequestItemDTO> itemDTOs =
                new ArrayList<>();

        if (request.getItems() != null) {

            for (IssueRequestItem item :
                    request.getItems()) {

                RequestItemDTO itemDTO =
                        new RequestItemDTO();

                itemDTO.setItemId(
                        item.getId()
                );

                itemDTO.setDurationDays(
                        item.getDurationDays()
                );

                itemDTO.setStatus(
                        item.getStatus()
                );

                itemDTO.setApprovedAt(
                        item.getApprovedAt()
                );

                itemDTO.setIssuedAt(
                        item.getIssuedAt()
                );

                itemDTO.setDueDate(
                        item.getDueDate()
                );

                itemDTO.setReturnedAt(
                        item.getReturnedAt()
                );

                itemDTO.setRejectionReason(
                        item.getRejectionReason()
                );

                if (item.getBook() != null) {

                    itemDTO.setBookId(
                            item.getBook().getId()
                    );

                    itemDTO.setBookTitle(
                            item.getBook().getTitle()
                    );

                    itemDTO.setAuthor(
                            item.getBook().getAuthor()
                    );

                    itemDTO.setIsbn(
                            item.getBook().getIsbn()
                    );
                }

                if (item.getReissueOfItem() != null) {

                    IssueRequestItem originalItem =
                            item.getReissueOfItem();

                    itemDTO.setReissueOfItemId(
                            originalItem.getId()
                    );

                    itemDTO.setCurrentDueDate(
                            originalItem.getDueDate()
                    );
                }

                itemDTOs.add(itemDTO);
            }
        }

        dto.setItems(itemDTOs);

        return dto;
    }

// Filter Requests

private boolean filterRequests(
        IssueRequest request,
        String search,
        String status) {

    boolean statusMatches = true;

    if (status != null
            && !status.isBlank()
            && !"ALL".equalsIgnoreCase(status)) {

        String statusValue =
                status.trim().toUpperCase();

        String requestStatus =
                request.getStatus() != null
                        ? request.getStatus().toUpperCase()
                        : "";

        boolean requestStatusMatches =
                statusValue.equals(requestStatus);

        boolean itemStatusMatches = false;

        if (request.getItems() != null) {

            itemStatusMatches =
                    request.getItems()
                            .stream()
                            .anyMatch(item -> {

                                if (item.getStatus() == null) {
                                    return false;
                                }

                                return statusValue.equals(
                                        item.getStatus()
                                                .toUpperCase()
                                );
                            });
        }

        if ("PENDING".equals(statusValue)) {

            boolean normalPendingItemExists =
                    request.getItems() != null
                            && request.getItems()
                            .stream()
                            .anyMatch(item ->
                                    "PENDING".equals(
                                            item.getStatus()
                                    )
                                            && request.getRequestType() != null
                                            && !"REISSUE".equals(
                                                    request.getRequestType()
                                            )
                            );

            statusMatches =
                    "PENDING".equals(requestStatus)
                            || "PARTIALLY_APPROVED".equals(
                                    requestStatus
                            )
                            || normalPendingItemExists;

        } else {

            statusMatches =
                    requestStatusMatches
                            || itemStatusMatches;
        }
    }

    if (!statusMatches) {
        return false;
    }

    if (search == null
            || search.isBlank()) {

        return true;
    }

    String searchValue =
            search.trim().toLowerCase();

    if (request.getStudent() != null) {

        String studentName =
                request.getStudent().getStudentName();

        String rollNumber =
                request.getStudent().getRollNumber();

        if (studentName != null
                && studentName.toLowerCase()
                .contains(searchValue)) {

            return true;
        }

        if (rollNumber != null
                && rollNumber.toLowerCase()
                .contains(searchValue)) {

            return true;
        }
    }

    if (request.getItems() != null) {

        for (IssueRequestItem item :
                request.getItems()) {

            if (item.getBook() == null) {
                continue;
            }

            String title =
                    item.getBook().getTitle();

            String author =
                    item.getBook().getAuthor();

            String isbn =
                    item.getBook().getIsbn();

            if (title != null
                    && title.toLowerCase()
                    .contains(searchValue)) {

                return true;
            }

            if (author != null
                    && author.toLowerCase()
                    .contains(searchValue)) {

                return true;
            }

            if (isbn != null
                    && isbn.toLowerCase()
                    .contains(searchValue)) {

                return true;
            }
        }
    }

    return false;
}
}        