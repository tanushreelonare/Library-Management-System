package com.example.library.repository;

import com.example.library.entity.Librarian;
import com.example.library.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LibrarianRepository extends JpaRepository<Librarian, Long> {

    // EMPLOYEE ID

    Optional<Librarian> findByEmployeeId(String employeeId);

    boolean existsByEmployeeId(String employeeId);

    // MOBILE NUMBER

    Optional<Librarian> findByMobile(String mobile);

    boolean existsByMobile(String mobile);

    // USER ACCOUNT

    Optional<Librarian> findByUser(User user);

    Optional<Librarian> findByUserId(Long userId);

}