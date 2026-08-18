package com.example.library.service;

import com.example.library.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    // READ OPERATIONS

    /**
     * Returns all users.
     *
     * @return List of users
     */
    List<User> getAllUsers();

    /**
     * Returns user by database ID.
     *
     * @param id User ID
     * @return Optional User
     */
    Optional<User> getUserById(Long id);

    /**
     * Returns user by username.
     *
     * @param username Username
     * @return Optional User
     */
    
    Optional<User> getUserByEmail(String email);

    boolean existsByEmail(String email);

    // PASSWORD MANAGEMENT

    /**
     * Updates user password.
     *
     * @param userId User ID
     * @param newPassword Encoded password
     */
    void updatePassword(
            Long userId,
            String newPassword
    );

    // ACCOUNT STATUS

    /**
     * Enables or disables a user account.
     *
     * @param userId User ID
     * @param enabled true = enabled
     */
    void updateAccountStatus(
            Long userId,
            boolean enabled
    );

    /**
     * Locks or unlocks a user account.
     *
     * @param userId User ID
     * @param locked true = locked
     */
    void updateAccountLockStatus(
            Long userId,
            boolean locked
    );

    // DELETE

    /**
     * Deletes a user.
     *
     * @param userId User ID
     */
    void deleteUser(Long userId);

}