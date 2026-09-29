package com.example.library.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

public class StudentProfileDTO {

    // READ-ONLY PERSONAL INFORMATION

    private Long id;

    private String rollNumber;

    // EDITABLE PERSONAL INFORMATION

    @NotBlank(
            message = "Student Name is required."
    )
    @Size(
            min = 3,
            max = 100,
            message = "Student Name must be between 3 and 100 characters."
    )
    private String studentName;

    @NotBlank(
            message = "Mobile Number is required."
    )
    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "Mobile Number must contain exactly 10 digits."
    )
    private String mobile;

    @NotBlank(
            message = "Branch is required."
    )
    private String branch;

    @NotNull(
            message = "Semester is required."
    )
    @Min(
            value = 1,
            message = "Semester must be between 1 and 10."
    )
    @Max(
            value = 10,
            message = "Semester must be between 1 and 10."
    )
    private Integer semester;

    @NotBlank(
            message = "Address is required."
    )
    private String address;

    // PROFILE IMAGE

    private MultipartFile studentImage;

    private String imagePath;

    // READ-ONLY ACCOUNT INFORMATION

    private String email;

    private boolean enabled;

    private LocalDateTime createdAt;

    // CONSTRUCTOR

    public StudentProfileDTO() {
    }

    // ID

    public Long getId() {

        return id;

    }

    public void setId(
            Long id) {

        this.id = id;

    }

    // ROLL NUMBER

    public String getRollNumber() {

        return rollNumber;

    }

    public void setRollNumber(
            String rollNumber) {

        this.rollNumber = rollNumber;

    }

    // STUDENT NAME

    public String getStudentName() {

        return studentName;

    }

    public void setStudentName(
            String studentName) {

        this.studentName = studentName;

    }

    // MOBILE

    public String getMobile() {

        return mobile;

    }

    public void setMobile(
            String mobile) {

        this.mobile = mobile;

    }

    // BRANCH

    public String getBranch() {

        return branch;

    }

    public void setBranch(
            String branch) {

        this.branch = branch;

    }

    // SEMESTER

    public Integer getSemester() {

        return semester;

    }

    public void setSemester(
            Integer semester) {

        this.semester = semester;

    }

    // ADDRESS

    public String getAddress() {

        return address;

    }

    public void setAddress(
            String address) {

        this.address = address;

    }

    // STUDENT IMAGE

    public MultipartFile getStudentImage() {

        return studentImage;

    }

    public void setStudentImage(
            MultipartFile studentImage) {

        this.studentImage = studentImage;

    }

    // IMAGE PATH

    public String getImagePath() {

        return imagePath;

    }

    public void setImagePath(
            String imagePath) {

        this.imagePath = imagePath;

    }

    // REGISTERED EMAIL

    public String getEmail() {

        return email;

    }

    public void setEmail(
            String email) {

        this.email = email;

    }

    // ACCOUNT STATUS

    public boolean isEnabled() {

        return enabled;

    }

    public void setEnabled(
            boolean enabled) {

        this.enabled = enabled;

    }

    // REGISTRATION DATE

    public LocalDateTime getCreatedAt() {

        return createdAt;

    }

    public void setCreatedAt(
            LocalDateTime createdAt) {

        this.createdAt = createdAt;

    }

}