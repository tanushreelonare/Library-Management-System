package com.example.library.controller;

import com.example.library.dto.StudentReportDTO;
import com.example.library.entity.Student;
import com.example.library.service.StudentService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/librarian/students")
public class LibrarianStudentController {

    private final StudentService studentService;

    public LibrarianStudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // Student report page
    @GetMapping
    public String studentReport(
            @RequestParam(value = "search", required = false) String search,
            Model model) {

        List<StudentReportDTO> students;

        if (search == null || search.trim().isEmpty()) {
            students = studentService.getStudentReport();
        } else {
            students = studentService.searchStudents(search);
        }

        model.addAttribute("students", students);
        model.addAttribute("search", search);

        return "librarian-students";
    }

    // Get student details for edit modal
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<StudentReportDTO> getStudent(
            @PathVariable Long id) {

        StudentReportDTO student =
                studentService.getStudentReportById(id);

        return ResponseEntity.ok(student);
    }

    // Update student from edit modal
    @PostMapping("/{id}/update")
    @ResponseBody
    public ResponseEntity<?> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody StudentReportDTO dto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {

            return ResponseEntity.badRequest()
                    .body("Please enter valid student details.");
        }

        try {

            Student updatedStudent =
                    studentService.updateStudentFromReport(id, dto);

            StudentReportDTO updatedDTO =
                    studentService.getStudentReportById(
                            updatedStudent.getId());

            return ResponseEntity.ok(updatedDTO);

        } catch (IllegalArgumentException e) {

            return ResponseEntity.badRequest()
                    .body(e.getMessage());

        } catch (Exception e) {

            return ResponseEntity.internalServerError()
                    .body("Unable to update student.");
        }
    }

    // Activate or deactivate student
    @PostMapping("/{id}/toggle-status")
    @ResponseBody
    public ResponseEntity<?> toggleStudentStatus(
            @PathVariable Long id) {

        try {

            studentService.toggleStudentStatus(id);

            StudentReportDTO student =
                    studentService.getStudentReportById(id);

            return ResponseEntity.ok(student);

        } catch (Exception e) {

            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

    // Permanently delete student
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteStudent(
            @PathVariable Long id) {

        try {

            studentService.deleteStudent(id);

            return ResponseEntity.ok(
                    "Student deleted successfully.");

        } catch (Exception e) {

            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }
}