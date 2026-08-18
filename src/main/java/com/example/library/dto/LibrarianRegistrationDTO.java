package com.example.library.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

public class LibrarianRegistrationDTO {

    // USER ACCOUNT INFORMATION

    @NotBlank(message = "Email is required.")
    @Email(message = "Enter a valid Email Address.")
    private String email;

    @NotBlank(message = "Password is required.")
    @Size(
            min = 8,
            message = "Password must contain at least 8 characters."
    )
    private String password;

    @NotBlank(message = "Confirm Password is required.")
    private String confirmPassword;

    // LIBRARIAN INFORMATION

    @NotBlank(message = "Employee ID is required.")
    @Size(
            max = 20,
            message = "Employee ID cannot exceed 20 characters."
    )
    private String employeeId;

    @NotBlank(message = "Librarian Name is required.")
    @Size(
            min = 3,
            max = 100,
            message = "Name must be between 3 and 100 characters."
    )
    private String librarianName;

    @NotBlank(message = "Mobile Number is required.")
    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "Mobile Number must contain exactly 10 digits."
    )
    private String mobile;

    @NotBlank(message = "Designation is required.")
    private String designation;

    @NotBlank(message = "Qualification is required.")
    private String qualification;

    @NotNull(message = "Experience is required.")
    @Min(
            value = 0,
            message = "Experience cannot be negative."
    )
    @Max(
            value = 50,
            message = "Experience cannot exceed 50 years."
    )
    private Integer experience;

    @NotBlank(message = "Address is required.")
    private String address;

    // IMAGE

    private MultipartFile librarianImage;

    // CONSTRUCTOR

    public LibrarianRegistrationDTO() {
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

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getLibrarianName() {
        return librarianName;
    }

    public void setLibrarianName(String librarianName) {
        this.librarianName = librarianName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public MultipartFile getLibrarianImage() {
        return librarianImage;
    }

    public void setLibrarianImage(MultipartFile librarianImage) {
        this.librarianImage = librarianImage;
    }

}