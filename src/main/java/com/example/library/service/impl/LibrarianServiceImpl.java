package com.example.library.service.impl;

import com.example.library.dto.LibrarianRegistrationDTO;
import com.example.library.entity.Librarian;
import com.example.library.entity.User;
import com.example.library.enums.Role;
import com.example.library.repository.LibrarianRepository;
import com.example.library.repository.UserRepository;
import com.example.library.service.FileStorageService;
import com.example.library.service.LibrarianService;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LibrarianServiceImpl implements LibrarianService {

    // DEPENDENCIES

    private final LibrarianRepository librarianRepository;

    private final UserRepository userRepository;

    private final FileStorageService fileStorageService;

    private final PasswordEncoder passwordEncoder;

    // CONSTRUCTOR

    public LibrarianServiceImpl(
            LibrarianRepository librarianRepository,
            UserRepository userRepository,
            FileStorageService fileStorageService,
            PasswordEncoder passwordEncoder) {

        this.librarianRepository = librarianRepository;
        this.userRepository = userRepository;
        this.fileStorageService = fileStorageService;
        this.passwordEncoder = passwordEncoder;
    }

    // REGISTRATION

    @Override
    public Librarian registerLibrarian(
            LibrarianRegistrationDTO dto)
            throws IOException {

        // Validate password

        validatePasswords(
                dto.getPassword(),
                dto.getConfirmPassword()
        );

        // Validate duplicate data

        validateDuplicateLibrarian(
                dto,
                null,
                null
        );

        // Upload profile image

        String imagePath =
                fileStorageService.storeLibrarianImage(
                        dto.getLibrarianImage()
                );

        // Create User

        User user = new User();

        user.setEmail(
                dto.getEmail()
        );

        user.setPassword(
                passwordEncoder.encode(
                        dto.getPassword()
                )
        );

        user.setRole(
                Role.LIBRARIAN
        );

        user.setEnabled(true);

        user.setAccountNonExpired(true);

        user.setAccountNonLocked(true);

        user.setCredentialsNonExpired(true);

        // Save User first

        user = userRepository.save(user);

        // Create Librarian

        Librarian librarian = new Librarian();

        librarian.setEmployeeId(
                dto.getEmployeeId()
        );

        librarian.setLibrarianName(
                dto.getLibrarianName()
        );

        // IMPORTANT:
        // Store email in librarians table as well.

        librarian.setEmail(
                dto.getEmail()
        );

        librarian.setMobile(
                dto.getMobile()
        );

        librarian.setDesignation(
                dto.getDesignation()
        );

        librarian.setQualification(
                dto.getQualification()
        );

        librarian.setExperience(
                dto.getExperience()
        );

        librarian.setAddress(
                dto.getAddress()
        );

        librarian.setImagePath(
                imagePath
        );

        librarian.setActive(true);

        // Establish relationship

        librarian.setUser(user);

        user.setLibrarian(librarian);

        // Save Librarian

        return librarianRepository.save(
                librarian
        );
    }

    // READ OPERATIONS

    @Override
    public List<Librarian> getAllLibrarians() {

        return librarianRepository.findAll();
    }


    @Override
    public Optional<Librarian> getLibrarianById(
            Long id) {

        return librarianRepository.findById(id);
    }


    @Override
    public Optional<Librarian> getLibrarianByEmployeeId(
            String employeeId) {

        return librarianRepository.findByEmployeeId(
                employeeId
        );
    }


    @Override
    public Optional<Librarian> getLibrarianByUserId(
            Long userId) {

        return librarianRepository.findByUserId(
                userId
        );
    }

    // VALIDATION

    @Override
    public boolean existsByEmployeeId(
            String employeeId) {

        return librarianRepository.existsByEmployeeId(
                employeeId
        );
    }


    @Override
    public boolean existsByMobile(
            String mobile) {

        return librarianRepository.existsByMobile(
                mobile
        );
    }

    // UPDATE

    @Override
    public Librarian updateLibrarian(
            Long id,
            LibrarianRegistrationDTO dto)
            throws IOException {

        // Find existing Librarian

        Librarian librarian =
                librarianRepository.findById(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Librarian not found with ID : "
                                                + id
                                )
                        );

        // Get associated User

        User user = librarian.getUser();

        // Validate password

        validatePasswords(
                dto.getPassword(),
                dto.getConfirmPassword()
        );

        // Validate duplicate data

        validateDuplicateLibrarian(
                dto,
                librarian,
                user
        );

        // Replace image only if a new image is uploaded

        if (dto.getLibrarianImage() != null
                && !dto.getLibrarianImage().isEmpty()) {

            if (librarian.getImagePath() != null
                    && !librarian.getImagePath().isBlank()) {

                fileStorageService.deleteFile(
                        librarian.getImagePath()
                );
            }

            String imagePath =
                    fileStorageService.storeLibrarianImage(
                            dto.getLibrarianImage()
                    );

            librarian.setImagePath(
                    imagePath
            );
        }

        // Update User

        user.setEmail(
                dto.getEmail()
        );

        user.setPassword(
                passwordEncoder.encode(
                        dto.getPassword()
                )
        );

        // Update Librarian

        librarian.setEmployeeId(
                dto.getEmployeeId()
        );

        librarian.setLibrarianName(
                dto.getLibrarianName()
        );

        // IMPORTANT:
        // Keep librarians.email synchronized with users.email.

        librarian.setEmail(
                dto.getEmail()
        );

        librarian.setMobile(
                dto.getMobile()
        );

        librarian.setDesignation(
                dto.getDesignation()
        );

        librarian.setQualification(
                dto.getQualification()
        );

        librarian.setExperience(
                dto.getExperience()
        );

        librarian.setAddress(
                dto.getAddress()
        );

        librarian.setActive(true);

        // Save User

        userRepository.save(
                user
        );

        // Save Librarian

        return librarianRepository.save(
                librarian
        );
    }

    // DELETE

    @Override
    public void deleteLibrarian(
            Long id) {

        Librarian librarian =
                librarianRepository.findById(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Librarian not found with ID : "
                                                + id
                                )
                        );

        // Delete profile image

        try {

            if (librarian.getImagePath() != null
                    && !librarian.getImagePath().isBlank()) {

                fileStorageService.deleteFile(
                        librarian.getImagePath()
                );
            }

        } catch (IOException ignored) {

            // Continue database deletion even if
            // physical image deletion fails.

        }

        // Get associated User

        User user =
                librarian.getUser();

        // Delete Librarian

        librarianRepository.delete(
                librarian
        );

        // Delete associated User

        if (user != null) {

            userRepository.delete(
                    user
            );
        }
    }

    // HELPER - PASSWORD VALIDATION

    private void validatePasswords(
            String password,
            String confirmPassword) {

        if (password == null
                || password.isBlank()) {

            throw new IllegalArgumentException(
                    "Password is required."
            );
        }


        if (confirmPassword == null
                || confirmPassword.isBlank()) {

            throw new IllegalArgumentException(
                    "Confirm Password is required."
            );
        }


        if (!password.equals(confirmPassword)) {

            throw new IllegalArgumentException(
                    "Password and Confirm Password do not match."
            );
        }
    }

    // HELPER - DUPLICATE VALIDATION

    private void validateDuplicateLibrarian(
            LibrarianRegistrationDTO dto,
            Librarian existingLibrarian,
            User existingUser) {

        // EMAIL

        if (existingUser == null) {

            if (userRepository.existsByEmail(
                    dto.getEmail())) {

                throw new IllegalArgumentException(
                        "Email already exists."
                );
            }

        } else if (
                !existingUser.getEmail().equals(
                        dto.getEmail()
                )
                        &&
                        userRepository.existsByEmail(
                                dto.getEmail()
                        )
        ) {

            throw new IllegalArgumentException(
                    "Email already exists."
            );
        }

        // EMPLOYEE ID

        if (existingLibrarian == null) {

            if (librarianRepository.existsByEmployeeId(
                    dto.getEmployeeId())) {

                throw new IllegalArgumentException(
                        "Employee ID already exists."
                );
            }

        } else if (
                !existingLibrarian.getEmployeeId().equals(
                        dto.getEmployeeId()
                )
                        &&
                        librarianRepository.existsByEmployeeId(
                                dto.getEmployeeId()
                        )
        ) {

            throw new IllegalArgumentException(
                    "Employee ID already exists."
            );
        }

        // MOBILE NUMBER

        if (existingLibrarian == null) {

            if (librarianRepository.existsByMobile(
                    dto.getMobile())) {

                throw new IllegalArgumentException(
                        "Mobile Number already exists."
                );
            }

        } else if (
                !existingLibrarian.getMobile().equals(
                        dto.getMobile()
                )
                        &&
                        librarianRepository.existsByMobile(
                                dto.getMobile()
                        )
        ) {

            throw new IllegalArgumentException(
                    "Mobile Number already exists."
            );
        }
    }
}