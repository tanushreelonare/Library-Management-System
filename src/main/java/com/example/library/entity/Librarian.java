package com.example.library.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(
        name = "librarians",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "employee_id"),
                @UniqueConstraint(columnNames = "mobile")
        }
)
public class Librarian extends BaseEntity {

    
    // USER ACCOUNT
    

    @OneToOne(fetch = FetchType.LAZY,
              cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "user_id",
            nullable = false,
            unique = true
    )
    private User user;

    // ==========================================
    // EMPLOYEE ID
    // ==========================================

    @NotBlank(message = "Employee ID is required.")
    @Size(max = 20,
            message = "Employee ID cannot exceed 20 characters.")
    @Column(
            name = "employee_id",
            nullable = false,
            unique = true,
            length = 20
    )
    private String employeeId;

    //Librarian Name
    
    @NotBlank(message = "Librarian Name is required.")
    @Size(
            min = 3,
            max = 100,
            message = "Name must be between 3 and 100 characters."
    )
    @Column(
            name = "librarian_name",
            nullable = false,
            length = 100
    )
    private String librarianName;

    //Email
    @NotBlank(message = "Email is required.")
    @Email(message = "Enter a valid Email Address.")
    @Column(
            nullable = false,
            unique = true,
            length = 255
    )
    private String email;

    //Mobile Number

    @NotBlank(message = "Mobile Number is required.")
    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "Mobile Number must contain exactly 10 digits."
    )
    @Column(
            nullable = false,
            unique = true,
            length = 10
    )
    private String mobile;

    //Designation

    @NotBlank(message = "Designation is required.")
    @Column(
            nullable = false,
            length = 50
    )
    private String designation;

    //Qaulification

    @NotBlank(message = "Qualification is required.")
    @Column(
            nullable = false,
            length = 100
    )
    private String qualification;

    //Experience

    @NotNull(message = "Experience is required.")
    @Min(
            value = 0,
            message = "Experience cannot be negative."
    )
    @Max(
            value = 50,
            message = "Experience cannot exceed 50 years."
    )
    @Column(nullable = false)
    private Integer experience;

    //Address

    @NotBlank(message = "Address is required.")
    @Column(
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String address;

    //Image Path

    @Column(name = "image_path")
    private String imagePath;

    //Account Status

    @Column(nullable = false)
    private boolean active = true;
    
    //Constructors

    public Librarian() {
    }

    //Getters and Setters

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public boolean isActive() {
        return active;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

}