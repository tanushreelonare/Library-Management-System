package com.example.library.service;

import com.example.library.dto.StudentRegistrationDTO;
import com.example.library.entity.Student;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface StudentService {

    // REGISTRATION

    /**
     * Registers a new student.
     *
     * @param studentRegistrationDTO Student registration details
     * @return Registered Student
     */
    Student registerStudent(StudentRegistrationDTO studentRegistrationDTO)
            throws IOException;

    // READ OPERATIONS

    /**
     * Returns all students.
     *
     * @return List of students
     */
    List<Student> getAllStudents();

    /**
     * Returns student by database ID.
     *
     * @param id Student ID
     * @return Optional Student
     */
    Optional<Student> getStudentById(Long id);

    /**
     * Returns student by roll number.
     *
     * @param rollNumber Roll Number
     * @return Optional Student
     */
    Optional<Student> getStudentByRollNumber(String rollNumber);

    /**
     * Returns student by associated user ID.
     *
     * @param userId User ID
     * @return Optional Student
     */
    Optional<Student> getStudentByUserId(Long userId);

    // UPDATE

    /**
     * Updates student profile.
     *
     * @param id Student ID
     * @param studentRegistrationDTO Updated details
     * @return Updated Student
     */
    Student updateStudent(Long id,
                          StudentRegistrationDTO studentRegistrationDTO)
            throws IOException;

    // DELETE

    /**
     * Deletes a student.
     *
     * @param id Student ID
     */
    void deleteStudent(Long id);

    // VALIDATION

    /**
     * Checks if roll number already exists.
     *
     * @param rollNumber Roll Number
     * @return true if exists
     */
    boolean existsByRollNumber(String rollNumber);

    /**
     * Checks if mobile number already exists.
     *
     * @param mobile Mobile Number
     * @return true if exists
     */
    boolean existsByMobile(String mobile);

}