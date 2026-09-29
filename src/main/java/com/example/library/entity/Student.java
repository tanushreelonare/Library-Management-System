package com.example.library.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(
        name = "students",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "roll_number"),
                @UniqueConstraint(columnNames = "mobile")
        }
)
public class Student extends BaseEntity {

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

    // ROLL NUMBER

    @NotBlank(message = "Roll Number is required.")
    @Size(max = 20,
            message = "Roll Number cannot exceed 20 characters.")
    @Column(name = "roll_number",
            nullable = false,
            unique = true,
            length = 20)
    private String rollNumber;

    // STUDENT NAME

    @NotBlank(message = "Student Name is required.")
    @Size(min = 3,
            max = 100,
            message = "Student Name must be between 3 and 100 characters.")
    @Column(name = "student_name",
            nullable = false,
            length = 100)
    private String studentName;

    // MOBILE

    @NotBlank(message = "Mobile Number is required.")
    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "Mobile Number must contain exactly 10 digits."
    )
    @Column(nullable = false,
            unique = true,
            length = 10)
    private String mobile;

    // BRANCH

    @NotBlank(message = "Branch is required.")
    @Column(nullable = false,
            length = 50)
    private String branch;

    // SEMESTER

    @NotNull(message = "Semester is required.")
    @Min(value = 1,
            message = "Semester must be between 1 and 10.")
    @Max(value = 10,
            message = "Semester must be between 1 and 10.")
    @Column(nullable = false)
    private Integer semester;

    // ADDRESS

    @NotBlank(message = "Address is required.")
    @Column(nullable = false,
            columnDefinition = "TEXT")
    private String address;

    // IMAGE PATH

    @Column(name = "image_path")
    private String imagePath;
    // Constructors

    public Student() {
    }

    // GETTERS & SETTERS

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

}