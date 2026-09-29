package com.example.library.service.impl;

import com.example.library.dto.StudentProfileDTO;
import com.example.library.dto.StudentRegistrationDTO;
import com.example.library.dto.StudentReportDTO;
import com.example.library.entity.Student;
import com.example.library.entity.User;
import com.example.library.enums.Role;
import com.example.library.repository.StudentRepository;
import com.example.library.repository.UserRepository;
import com.example.library.service.FileStorageService;
import com.example.library.service.StudentService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileStorageService fileStorageService;

    public StudentServiceImpl(
            StudentRepository studentRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            FileStorageService fileStorageService) {

        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.fileStorageService = fileStorageService;
    }

    // Register student
    @Override
    public Student registerStudent(StudentRegistrationDTO dto) throws IOException {

        validatePassword(dto);

        if (studentRepository.existsByRollNumber(dto.getRollNumber())) {
            throw new IllegalArgumentException("Roll number already exists");
        }

        if (studentRepository.existsByMobile(dto.getMobile())) {
            throw new IllegalArgumentException("Mobile number already exists");
        }

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        String imagePath = null;

        if (dto.getStudentImage() != null
                && !dto.getStudentImage().isEmpty()) {

            imagePath = fileStorageService.storeStudentImage(dto.getStudentImage());
        }

        User user = new User();

        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.STUDENT);
        user.setEnabled(true);

        user = userRepository.save(user);

        Student student = new Student();

        student.setRollNumber(dto.getRollNumber());
        student.setStudentName(dto.getStudentName());
        student.setMobile(dto.getMobile());
        student.setBranch(dto.getBranch());
        student.setSemester(dto.getSemester());
        student.setAddress(dto.getAddress());
        student.setImagePath(imagePath);
        student.setUser(user);

        return studentRepository.save(student);
    }

    // Get all students
    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // Get student by ID
    @Override
    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    // Get student by roll number
    @Override
    public Optional<Student> getStudentByRollNumber(String rollNumber) {
        return studentRepository.findByRollNumber(rollNumber);
    }

    // Get student by user ID
    @Override
    public Optional<Student> getStudentByUserId(Long userId) {
        return studentRepository.findByUserId(userId);
    }

    // Get student profile
    @Override
    public StudentProfileDTO getStudentProfile(Long userId) {

        Student student = studentRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Student profile not found"));

        User user = student.getUser();

        StudentProfileDTO dto = new StudentProfileDTO();

        dto.setId(student.getId());
        dto.setRollNumber(student.getRollNumber());
        dto.setStudentName(student.getStudentName());
        dto.setMobile(student.getMobile());
        dto.setBranch(student.getBranch());
        dto.setSemester(student.getSemester());
        dto.setAddress(student.getAddress());
        dto.setImagePath(student.getImagePath());

        if (user != null) {
            dto.setEmail(user.getEmail());
            dto.setEnabled(user.isEnabled());
        }

        dto.setCreatedAt(student.getCreatedAt());

        return dto;
    }

    // Update student profile
    @Override
    public Student updateStudentProfile(
            Long userId,
            StudentProfileDTO dto) throws IOException {

        Student student = studentRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Student profile not found"));

        if (studentRepository.existsByMobile(dto.getMobile())
                && !dto.getMobile().equals(student.getMobile())) {

            throw new IllegalArgumentException(
                    "Mobile number already exists");
        }

        if (dto.getStudentName() == null
                || dto.getStudentName().trim().length() < 3) {

            throw new IllegalArgumentException(
                    "Student name must contain at least 3 characters");
        }

        if (dto.getBranch() == null
                || dto.getBranch().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Branch is required");
        }

        if (dto.getSemester() == null
                || dto.getSemester() < 1
                || dto.getSemester() > 10) {

            throw new IllegalArgumentException(
                    "Semester must be between 1 and 10");
        }

        if (dto.getAddress() == null
                || dto.getAddress().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Address is required");
        }

        String oldImagePath = student.getImagePath();

        if (dto.getStudentImage() != null
                && !dto.getStudentImage().isEmpty()) {

            String newImagePath =
                    fileStorageService.storeStudentImage(dto.getStudentImage());

            student.setImagePath(newImagePath);

            if (oldImagePath != null
                    && !oldImagePath.isBlank()) {

                try {
                    fileStorageService.deleteFile(oldImagePath);
                } catch (IOException ignored) {
                    // Keep the new image even if old image deletion fails
                }
            }
        }

        student.setStudentName(dto.getStudentName().trim());
        student.setMobile(dto.getMobile());
        student.setBranch(dto.getBranch().trim());
        student.setSemester(dto.getSemester());
        student.setAddress(dto.getAddress().trim());

        return studentRepository.save(student);
    }

        // Get student report
    @Override
    public List<StudentReportDTO> getStudentReport() {

        List<Student> students = studentRepository.findAll();

        List<StudentReportDTO> report = new ArrayList<>();

        for (Student student : students) {
            report.add(convertToStudentReportDTO(student));
        }

        return report;
    }

    // Search students
    @Override
    public List<StudentReportDTO> searchStudents(String searchTerm) {

        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getStudentReport();
        }

        String keyword = searchTerm.trim();

        Map<Long, Student> uniqueStudents = new LinkedHashMap<>();

        studentRepository
                .findByRollNumberContainingIgnoreCase(keyword)
                .forEach(student ->
                        uniqueStudents.put(student.getId(), student));

        studentRepository
                .findByStudentNameContainingIgnoreCase(keyword)
                .forEach(student ->
                        uniqueStudents.put(student.getId(), student));

        studentRepository
                .findByMobileContaining(keyword)
                .forEach(student ->
                        uniqueStudents.put(student.getId(), student));

        studentRepository
                .findByUser_EmailContainingIgnoreCase(keyword)
                .forEach(student ->
                        uniqueStudents.put(student.getId(), student));

        List<StudentReportDTO> report = new ArrayList<>();

        for (Student student : uniqueStudents.values()) {
            report.add(convertToStudentReportDTO(student));
        }

        return report;
    }

    // Get student report by ID
    @Override
    public StudentReportDTO getStudentReportById(Long id) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Student not found with ID: " + id));

        return convertToStudentReportDTO(student);
    }

    // Update student from librarian report
    @Override
    public Student updateStudentFromReport(
            Long id,
            StudentReportDTO dto) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Student not found with ID: " + id));

        if (studentRepository.existsByMobile(dto.getMobile())
                && !dto.getMobile().equals(student.getMobile())) {

            throw new IllegalArgumentException(
                    "Mobile number already exists");
        }

        if (dto.getStudentName() == null
                || dto.getStudentName().trim().length() < 3) {

            throw new IllegalArgumentException(
                    "Student name must contain at least 3 characters");
        }

        if (dto.getMobile() == null
                || !dto.getMobile().matches("\\d{10}")) {

            throw new IllegalArgumentException(
                    "Mobile number must contain exactly 10 digits");
        }

        if (dto.getBranch() == null
                || dto.getBranch().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Branch is required");
        }

        if (dto.getSemester() == null
                || dto.getSemester() < 1
                || dto.getSemester() > 10) {

            throw new IllegalArgumentException(
                    "Semester must be between 1 and 10");
        }

        if (dto.getAddress() == null
                || dto.getAddress().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Address is required");
        }

        student.setStudentName(dto.getStudentName().trim());
        student.setMobile(dto.getMobile());
        student.setBranch(dto.getBranch().trim());
        student.setSemester(dto.getSemester());
        student.setAddress(dto.getAddress().trim());

        return studentRepository.save(student);
    }

    // Toggle student account status
        @Override
        public void toggleStudentStatus(Long studentId) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Student not found with ID: " + studentId));

        User user = student.getUser();

        if (user == null) {
                throw new EntityNotFoundException(
                        "User account not found for student");
        }

        user.setEnabled(!user.isEnabled());

        userRepository.save(user);
        }
    // Delete student permanently
    @Override
    public void deleteStudent(Long studentId) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Student not found with ID: " + studentId));

        String imagePath = student.getImagePath();

        if (imagePath != null && !imagePath.isBlank()) {
            try {
                fileStorageService.deleteFile(imagePath);
            } catch (IOException ignored) {
                // Continue deleting the student record
            }
        }

        studentRepository.delete(student);
    }

    // Convert Student entity to StudentReportDTO
    private StudentReportDTO convertToStudentReportDTO(
            Student student) {

        StudentReportDTO dto = new StudentReportDTO();

        dto.setId(student.getId());
        dto.setRollNumber(student.getRollNumber());
        dto.setStudentName(student.getStudentName());
        dto.setMobile(student.getMobile());
        dto.setBranch(student.getBranch());
        dto.setSemester(student.getSemester());
        dto.setAddress(student.getAddress());

        if (student.getUser() != null) {
            dto.setEmail(student.getUser().getEmail());
            dto.setEnabled(student.getUser().isEnabled());
        }

        return dto;
    }

        // Update student
    @Override
    public Student updateStudent(
            Long id,
            StudentRegistrationDTO dto) throws IOException {

        Student student = studentRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Student not found with ID: " + id));

        User user = student.getUser();

        if (user == null) {
            throw new EntityNotFoundException(
                    "User account not found for student");
        }

        // Validate email
        if (dto.getEmail() == null
                || dto.getEmail().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Email is required");
        }

        String newEmail = dto.getEmail().trim();

        if (!newEmail.equalsIgnoreCase(user.getEmail())
                && userRepository.existsByEmail(newEmail)) {

            throw new IllegalArgumentException(
                    "Email already exists");
        }

        // Validate roll number
        if (dto.getRollNumber() == null
                || dto.getRollNumber().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Roll number is required");
        }

        String newRollNumber = dto.getRollNumber().trim();

        if (!newRollNumber.equalsIgnoreCase(student.getRollNumber())
                && studentRepository.existsByRollNumber(newRollNumber)) {

            throw new IllegalArgumentException(
                    "Roll number already exists");
        }

        // Validate mobile
        if (dto.getMobile() == null
                || !dto.getMobile().matches("\\d{10}")) {

            throw new IllegalArgumentException(
                    "Mobile number must contain exactly 10 digits");
        }

        if (!dto.getMobile().equals(student.getMobile())
                && studentRepository.existsByMobile(dto.getMobile())) {

            throw new IllegalArgumentException(
                    "Mobile number already exists");
        }

        // Validate name
        if (dto.getStudentName() == null
                || dto.getStudentName().trim().length() < 3) {

            throw new IllegalArgumentException(
                    "Student name must contain at least 3 characters");
        }

        // Validate branch
        if (dto.getBranch() == null
                || dto.getBranch().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Branch is required");
        }

        // Validate semester
        if (dto.getSemester() == null
                || dto.getSemester() < 1
                || dto.getSemester() > 10) {

            throw new IllegalArgumentException(
                    "Semester must be between 1 and 10");
        }

        // Validate address
        if (dto.getAddress() == null
                || dto.getAddress().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Address is required");
        }

        // Update password only when provided
        if (dto.getPassword() != null
                && !dto.getPassword().trim().isEmpty()) {

            if (!dto.getPassword().equals(dto.getConfirmPassword())) {
                throw new IllegalArgumentException(
                        "Passwords do not match");
            }

            if (dto.getPassword().length() < 8) {
                throw new IllegalArgumentException(
                        "Password must contain at least 8 characters");
            }

            user.setPassword(
                    passwordEncoder.encode(dto.getPassword()));
        }

        // Update user email
        user.setEmail(newEmail);

        userRepository.save(user);

        // Handle profile image
        String oldImagePath = student.getImagePath();

        if (dto.getStudentImage() != null
                && !dto.getStudentImage().isEmpty()) {

            String newImagePath =
                    fileStorageService.storeStudentImage(dto.getStudentImage());

            student.setImagePath(newImagePath);

            if (oldImagePath != null
                    && !oldImagePath.isBlank()) {

                try {
                    fileStorageService.deleteFile(oldImagePath);
                } catch (IOException ignored) {
                    // Keep the new image even if old image deletion fails
                }
            }
        }

        // Update student fields
        student.setRollNumber(newRollNumber);
        student.setStudentName(dto.getStudentName().trim());
        student.setMobile(dto.getMobile());
        student.setBranch(dto.getBranch().trim());
        student.setSemester(dto.getSemester());
        student.setAddress(dto.getAddress().trim());

        return studentRepository.save(student);
    }

    // Check roll number
    @Override
    public boolean existsByRollNumber(String rollNumber) {
        return studentRepository.existsByRollNumber(rollNumber);
    }

    // Check mobile number
    @Override
    public boolean existsByMobile(String mobile) {
        return studentRepository.existsByMobile(mobile);
    }

    // Validate registration password
    private void validatePassword(StudentRegistrationDTO dto) {

        if (dto.getPassword() == null
                || dto.getPassword().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Password is required");
        }

        if (dto.getConfirmPassword() == null
                || dto.getConfirmPassword().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Confirm password is required");
        }

        if (!dto.getPassword().equals(dto.getConfirmPassword())) {

            throw new IllegalArgumentException(
                    "Passwords do not match");
        }

        if (dto.getPassword().length() < 8) {

            throw new IllegalArgumentException(
                    "Password must contain at least 8 characters");
        }
    }
}

