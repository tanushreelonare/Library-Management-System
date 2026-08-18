package com.example.library.service.impl;

import com.example.library.dto.StudentRegistrationDTO;
import com.example.library.entity.Student;
import com.example.library.entity.User;
import com.example.library.enums.Role;
import com.example.library.repository.StudentRepository;
import com.example.library.repository.UserRepository;
import com.example.library.service.FileStorageService;
import com.example.library.service.StudentService;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {

    // Dependencies

    private final StudentRepository studentRepository;

    private final UserRepository userRepository;

    private final FileStorageService fileStorageService;

    private final PasswordEncoder passwordEncoder;

    // Constructor

    public StudentServiceImpl(
            StudentRepository studentRepository,
            UserRepository userRepository,
            FileStorageService fileStorageService,
            PasswordEncoder passwordEncoder) {

        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.fileStorageService = fileStorageService;
        this.passwordEncoder = passwordEncoder;
    }

    // Registration

@Override
public Student registerStudent(
        StudentRegistrationDTO dto)
        throws IOException {

    // Validate password

    validatePasswords(
            dto.getPassword(),
            dto.getConfirmPassword()
    );

    // Validate duplicate data

    validateDuplicateStudent(
            dto,
            null,
            null
    );

    // Upload profile image

    String imagePath = fileStorageService.storeStudentImage(
            dto.getStudentImage()
    );

    // Create User

    User user = new User();

    user.setEmail(dto.getEmail());

    user.setPassword(
            passwordEncoder.encode(dto.getPassword())
    );

    user.setRole(Role.STUDENT);

    user.setEnabled(true);

    user.setAccountNonExpired(true);

    user.setAccountNonLocked(true);

    user.setCredentialsNonExpired(true);

    // Create Student

    Student student = new Student();

    student.setRollNumber(dto.getRollNumber());

    student.setStudentName(dto.getStudentName());

    student.setMobile(dto.getMobile());

    student.setBranch(dto.getBranch());

    student.setSemester(dto.getSemester());

    student.setAddress(dto.getAddress());

    student.setImagePath(imagePath);

    // Establish relationship

    student.setUser(user);

    user.setStudent(student);

    // Persist

    return studentRepository.save(student);

}

// Read Operations

@Override
public List<Student> getAllStudents() {

    return studentRepository.findAll();

}

@Override
public Optional<Student> getStudentById(Long id) {

    return studentRepository.findById(id);

}

@Override
public Optional<Student> getStudentByRollNumber(
        String rollNumber) {

    return studentRepository.findByRollNumber(
            rollNumber
    );

}

@Override
public Optional<Student> getStudentByUserId(
        Long userId) {

    return studentRepository.findByUserId(
            userId
    );

}

// Validation

@Override
public boolean existsByRollNumber(
        String rollNumber) {

    return studentRepository.existsByRollNumber(
            rollNumber
    );

}

@Override
public boolean existsByMobile(
        String mobile) {

    return studentRepository.existsByMobile(
            mobile
    );

}

// Update

@Override
public Student updateStudent(
        Long id,
        StudentRegistrationDTO dto)
        throws IOException {

    Student student = studentRepository.findById(id)

            .orElseThrow(() ->
                    new EntityNotFoundException(
                            "Student not found with ID : " + id
                    ));

    User user = student.getUser();

    // Validate password

    validatePasswords(
            dto.getPassword(),
            dto.getConfirmPassword()
    );

    // Validate duplicate data

    validateDuplicateStudent(
            dto,
            student,
            user
    );

    // Replace image only if a new image is uploaded

    if (dto.getStudentImage() != null
            && !dto.getStudentImage().isEmpty()) {

        if (student.getImagePath() != null
                && !student.getImagePath().isBlank()) {

            fileStorageService.deleteFile(
                    student.getImagePath()
            );

        }

        // Upload image

        String imagePath = null;

        if (dto.getStudentImage() != null
                && !dto.getStudentImage().isEmpty()) {

        imagePath = fileStorageService.storeStudentImage(
                dto.getStudentImage()
        );

        }

        student.setImagePath(imagePath);

    }

    // Update Student

    student.setRollNumber(dto.getRollNumber());

    student.setStudentName(dto.getStudentName());

    student.setMobile(dto.getMobile());

    student.setBranch(dto.getBranch());

    student.setSemester(dto.getSemester());

    student.setAddress(dto.getAddress());

    // Save

    return studentRepository.save(student);

}

// Delete

@Override
public void deleteStudent(Long id) {

    Student student = studentRepository.findById(id)

            .orElseThrow(() ->
                    new EntityNotFoundException(
                            "Student not found with ID : " + id
                    ));

    try {

        if (student.getImagePath() != null
                && !student.getImagePath().isBlank()) {

            fileStorageService.deleteFile(
                    student.getImagePath()
            );

        }

    } catch (IOException ignored) {

    }

    studentRepository.delete(student);

}

// Helper Methods

private void validatePasswords(
        String password,
        String confirmPassword) {

    if (password == null || password.isBlank()) {

        throw new IllegalArgumentException(
                "Password is required."
        );

    }

    if (confirmPassword == null || confirmPassword.isBlank()) {

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

private void validateDuplicateStudent(
        StudentRegistrationDTO dto,
        Student existingStudent,
        User existingUser)
    {  

    // Email

    if (existingUser == null) {

        if (userRepository.existsByEmail(dto.getEmail())) {

            throw new IllegalArgumentException(
                    "Email already exists."
            );

        }

    } else if (!existingUser.getEmail().equals(dto.getEmail())
            && userRepository.existsByEmail(dto.getEmail())) {

        throw new IllegalArgumentException(
                "Email already exists."
        );

    }

    // Roll Number

    if (existingStudent == null) {

        if (studentRepository.existsByRollNumber(dto.getRollNumber())) {

            throw new IllegalArgumentException(
                    "Roll Number already exists."
            );

        }

    } else if (!existingStudent.getRollNumber().equals(dto.getRollNumber())
            && studentRepository.existsByRollNumber(dto.getRollNumber())) {

        throw new IllegalArgumentException(
                "Roll Number already exists."
        );

    }

    // Mobile

    if (existingStudent == null) {

        if (studentRepository.existsByMobile(dto.getMobile())) {

            throw new IllegalArgumentException(
                    "Mobile Number already exists."
            );

        }

    } else if (!existingStudent.getMobile().equals(dto.getMobile())
            && studentRepository.existsByMobile(dto.getMobile())) {

        throw new IllegalArgumentException(
                "Mobile Number already exists."
        );

    }
}

}