package com.example.library.repository;

import com.example.library.entity.Student;
import com.example.library.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    // ROLL NUMBER

    Optional<Student> findByRollNumber(String rollNumber);

    boolean existsByRollNumber(String rollNumber);

    // MOBILE NUMBER

    Optional<Student> findByMobile(String mobile);

    boolean existsByMobile(String mobile);

    // USER ACCOUNT

    Optional<Student> findByUser(User user);

    Optional<Student> findByUserId(Long userId);

}