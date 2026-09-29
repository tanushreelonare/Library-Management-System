package com.example.library.dto;

import jakarta.validation.constraints.*;

import org.springframework.web.multipart.MultipartFile;

public class StudentRegistrationDTO {

    // USER ACCOUNT INFORMATION

    @NotBlank(message = "Email is required.")
    @Email(message = "Enter a valid Email Address.")
    private String email;

    @NotBlank(message = "Password is required.")
    @Size(min = 8,
            message = "Password must contain at least 8 characters.")
    private String password;

    @NotBlank(message = "Confirm Password is required.")
    private String confirmPassword;

    // STUDENT INFORMATION

    @NotBlank(message = "Roll Number is required.")
    @Size(max = 20,
            message = "Roll Number cannot exceed 20 characters.")
    private String rollNumber;

    @NotBlank(message = "Student Name is required.")
    @Size(min = 3,
            max = 100,
            message = "Student Name must be between 3 and 100 characters.")
    private String studentName;

    @NotBlank(message = "Mobile Number is required.")
    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "Mobile Number must contain exactly 10 digits."
    )
    private String mobile;

    @NotBlank(message = "Branch is required.")
    private String branch;

    @NotNull(message = "Semester is required.")
    @Min(value = 1,
            message = "Semester must be between 1 and 10.")
    @Max(value = 10,
            message = "Semester must be between 1 and 10.")
    private Integer semester;

    @NotBlank(message = "Address is required.")
    private String address;

    // IMAGE

    private MultipartFile studentImage;

    // CONSTRUCTORS

    public StudentRegistrationDTO() {
    }

    // GETTERS & SETTERS

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
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

    public MultipartFile getStudentImage() {
        return studentImage;
    }

    public void setStudentImage(MultipartFile studentImage) {
        this.studentImage = studentImage;
    }

}