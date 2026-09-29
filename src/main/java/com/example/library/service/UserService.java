package com.example.library.service;

import com.example.library.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    // READ OPERATIONS

    List<User> getAllUsers();

    Optional<User> getUserById(Long id);

    Optional<User> getUserByEmail(String email);

    boolean existsByEmail(String email);

    // PASSWORD MANAGEMENT

    void updatePassword(
            Long userId,
            String newPassword
    );

    // ACCOUNT STATUS

    void updateAccountStatus(
            Long userId,
            boolean enabled
    );

    void updateAccountLockStatus(
            Long userId,
            boolean locked
    );

    // DELETE

    void deleteUser(Long userId);

}