package com.example.library.service;

import com.example.library.dto.StudentProfileDTO;
import com.example.library.dto.StudentRegistrationDTO;
import com.example.library.dto.StudentReportDTO;
import com.example.library.entity.Student;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface StudentService {

    // REGISTRATION

    /**
     * Registers a new student.
     *
     * @param dto Student registration details
     * @return Registered student
     */
    Student registerStudent(
            StudentRegistrationDTO dto)
            throws IOException;

    // READ OPERATIONS

    List<Student> getAllStudents();

    Optional<Student> getStudentById(Long id);

    Optional<Student> getStudentByRollNumber(
            String rollNumber);

    Optional<Student> getStudentByUserId(
            Long userId);

    // STUDENT PROFILE

    StudentProfileDTO getStudentProfile(
            Long userId);

    Student updateStudentProfile(
            Long userId,
            StudentProfileDTO dto)
            throws IOException;

    // STUDENT REPORT

    /**
     * Returns all students for the librarian report.
     */
    List<StudentReportDTO> getStudentReport();

    /**
     * Searches students by roll number, name,
     * mobile number or email.
     */
    List<StudentReportDTO> searchStudents(
            String searchTerm);

    /**
     * Returns one student for the edit modal.
     */
    StudentReportDTO getStudentReportById(
            Long id);

    /**
     * Updates student details from the
     * librarian edit modal.
     */
    Student updateStudentFromReport(
            Long id,
            StudentReportDTO dto);

    /**
     * Activates or deactivates a student's
     * account.
     */
    void toggleStudentStatus(
            Long studentId);

    /**
     * Permanently deletes a student.
     */
    void deleteStudent(
            Long studentId);

    // EXISTING UPDATE

    Student updateStudent(
            Long id,
            StudentRegistrationDTO dto)
            throws IOException;

    // VALIDATION

    boolean existsByRollNumber(
            String rollNumber);

    boolean existsByMobile(
            String mobile);
}