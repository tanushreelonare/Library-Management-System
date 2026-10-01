package com.example.library.service;

import com.example.library.dto.DonationRequestDTO;

import java.util.List;

public interface DonationRequestService {

    /* Student: submit a donation request */

    DonationRequestDTO submitDonationRequest(
            Long studentId,
            DonationRequestDTO donationRequestDTO
    );


    /* Student: view own donation requests */

    List<DonationRequestDTO> getStudentDonationRequests(
            Long studentId
    );


    /* Librarian: view all donation requests */

    List<DonationRequestDTO> getAllDonationRequests();


    /* Librarian: view pending donation requests */

    List<DonationRequestDTO> getPendingDonationRequests();


    /* Librarian: search donation requests */

    List<DonationRequestDTO> searchDonationRequests(
            String search
    );


    /* Librarian: filter donation requests by status */

    List<DonationRequestDTO> getDonationRequestsByStatus(
            String status
    );


    /* Librarian: approve a donation request */

    DonationRequestDTO approveDonationRequest(
            Long requestId
    );


    /* Librarian: reject a donation request */

    DonationRequestDTO rejectDonationRequest(
            Long requestId,
            String rejectionReason
    );


    /* Get one donation request for preview */

    DonationRequestDTO getDonationRequestById(
            Long requestId
    );
}