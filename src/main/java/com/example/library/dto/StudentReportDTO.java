package com.example.library.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class StudentReportDTO {

    // STUDENT IDENTIFICATION

    private Long id;

    private String rollNumber;

    private String email;

    // EDITABLE STUDENT INFORMATION

    @NotBlank(message = "Student Name is required.")
    @Size(
            min = 3,
            max = 100,
            message = "Name must be between 3 and 100 characters."
    )
    private String studentName;

    @NotBlank(message = "Mobile Number is required.")
    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "Mobile Number must contain exactly 10 digits."
    )
    private String mobile;

    @NotBlank(message = "Branch is required.")
    @Size(
            max = 50,
            message = "Branch cannot exceed 50 characters."
    )
    private String branch;

    @NotNull(message = "Semester is required.")
    @Min(
            value = 1,
            message = "Semester must be at least 1."
    )
    @Max(
            value = 8,
            message = "Semester cannot exceed 8."
    )
    private Integer semester;

    @NotBlank(message = "Address is required.")
    private String address;

    // ACCOUNT STATUS

    private boolean enabled;

    // CONSTRUCTOR

    public StudentReportDTO() {
    }

    // GETTERS & SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}