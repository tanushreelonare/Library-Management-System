package com.example.library.service;

import com.example.library.dto.LibrarianProfileDTO;
import com.example.library.dto.LibrarianRegistrationDTO;
import com.example.library.entity.Librarian;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface LibrarianService {

    // REGISTRATION

    /**
     * Registers a new librarian.
     *
     * @param librarianRegistrationDTO Librarian registration details
     * @return Registered Librarian
     */
    Librarian registerLibrarian(
            LibrarianRegistrationDTO librarianRegistrationDTO)
            throws IOException;

    // READ OPERATIONS

    /**
     * Returns all librarians.
     *
     * @return List of librarians
     */
    List<Librarian> getAllLibrarians();

    /**
     * Returns librarian by database ID.
     *
     * @param id Librarian ID
     * @return Optional Librarian
     */
    Optional<Librarian> getLibrarianById(Long id);

    /**
     * Returns librarian by employee ID.
     *
     * @param employeeId Employee ID
     * @return Optional Librarian
     */
    Optional<Librarian> getLibrarianByEmployeeId(String employeeId);

    /**
     * Returns librarian by associated user ID.
     *
     * @param userId User ID
     * @return Optional Librarian
     */
    Optional<Librarian> getLibrarianByUserId(Long userId);

    // PROFILE

    /**
     * Returns librarian profile by associated user ID.
     *
     * @param userId Associated user ID
     * @return Librarian profile details
     */
    LibrarianProfileDTO getLibrarianProfile(Long userId);

    /**
     * Updates librarian profile.
     *
     * @param userId Associated user ID
     * @param dto Updated profile details
     * @return Updated Librarian
     */
    Librarian updateLibrarianProfile(
            Long userId,
            LibrarianProfileDTO dto)
            throws IOException;

    // UPDATE

    /**
     * Updates librarian.
     *
     * @param id Librarian ID
     * @param librarianRegistrationDTO Updated details
     * @return Updated Librarian
     */
    Librarian updateLibrarian(
            Long id,
            LibrarianRegistrationDTO librarianRegistrationDTO)
            throws IOException;

    // DELETE

    /**
     * Deletes a librarian.
     *
     * @param id Librarian ID
     */
    void deleteLibrarian(Long id);

    // VALIDATION

    /**
     * Checks if employee ID already exists.
     *
     * @param employeeId Employee ID
     * @return true if exists
     */
    boolean existsByEmployeeId(String employeeId);

    /**
     * Checks if mobile number already exists.
     *
     * @param mobile Mobile Number
     * @return true if exists
     */
    boolean existsByMobile(String mobile);
}