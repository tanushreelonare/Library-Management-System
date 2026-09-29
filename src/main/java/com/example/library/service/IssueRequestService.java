package com.example.library.service;

import com.example.library.dto.MyRequestDTO;

import java.util.List;
import java.util.Map;

public interface IssueRequestService {

    List<MyRequestDTO> getStudentRequests(
            String email,
            String search,
            String status
    );

    MyRequestDTO getStudentRequestById(
            Long requestId,
            String email
    );

    void cancelRequest(
            Long requestId,
            String email
    );

    void submitIssueRequest(
            String email,
            Map<Long, Integer> bookDurations
    );

    void submitReissueRequest(
            Long itemId,
            Integer durationDays,
            String email
    );

    List<MyRequestDTO> getAllLibrarianRequests(
            String search,
            String status
    );

    MyRequestDTO getLibrarianRequestById(
            Long requestId
    );

    void approveRequest(
            Long requestId
    );

    void rejectRequest(
            Long requestId,
            String rejectionReason
    );

    void approveRequestItems(
            Long requestId,
            List<Long> itemIds
    );

    void rejectRequestItems(
            Long requestId,
            List<Long> itemIds,
            String rejectionReason
    );

    void approveReissueRequest(
            Long requestId
    );

    void rejectReissueRequest(
            Long requestId,
            String rejectionReason
    );
}