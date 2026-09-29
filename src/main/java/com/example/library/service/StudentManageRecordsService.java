package com.example.library.service;

import com.example.library.dto.IssuedBookDTO;

import java.util.List;

public interface StudentManageRecordsService {

    List<IssuedBookDTO> getStudentIssuedBooks(String email);

    void returnBook(Long itemId, String email);

    void requestReissue(Long itemId, Integer durationDays, String email);
}