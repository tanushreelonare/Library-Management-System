package com.example.library.service.impl;

import com.example.library.entity.User;
import com.example.library.repository.UserRepository;
import com.example.library.service.UserService;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // READ OPERATIONS

    @Override
    public List<User> getAllUsers() {

        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(Long id) {

        return userRepository.findById(id);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {

        return userRepository.findByEmail(email);
    }

    // VALIDATION

    @Override
    public boolean existsByEmail(String email) {

        return userRepository.existsByEmail(email);
    }

    // PASSWORD MANAGEMENT

    @Override
    public void updatePassword(
            Long userId,
            String newPassword) {

        if (newPassword == null || newPassword.isBlank()) {

            throw new IllegalArgumentException(
                    "Password cannot be empty."
            );
        }

        User user = userRepository.findById(userId)

                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "User not found with ID : " + userId
                        )
                );

        user.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(user);
    }

    // ACCOUNT STATUS

    @Override
    public void updateAccountStatus(
            Long userId,
            boolean enabled) {

        User user = userRepository.findById(userId)

                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "User not found with ID : " + userId
                        )
                );

        user.setEnabled(enabled);

        userRepository.save(user);
    }

    // ACCOUNT LOCK

    @Override
    public void updateAccountLockStatus(
            Long userId,
            boolean locked) {

        User user = userRepository.findById(userId)

                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "User not found with ID : " + userId
                        )
                );

        /*
         * accountNonLocked = true
         * means the account is NOT locked.
         */

        user.setAccountNonLocked(!locked);

        userRepository.save(user);
    }

    // DELETE

    @Override
    public void deleteUser(Long userId) {

        User user = userRepository.findById(userId)

                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "User not found with ID : " + userId
                        )
                );

        userRepository.delete(user);
    }

}