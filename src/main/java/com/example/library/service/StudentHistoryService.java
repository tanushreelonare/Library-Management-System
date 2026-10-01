package com.example.library.service;

import com.example.library.dto.StudentHistoryDTO;

import java.util.List;

public interface StudentHistoryService {

    List<StudentHistoryDTO> getStudentHistory(
            String search
    );
}