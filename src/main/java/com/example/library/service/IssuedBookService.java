package com.example.library.service;

import com.example.library.dto.IssuedBookDTO;

import java.util.List;

public interface IssuedBookService {

    // Get all currently issued books

    List<IssuedBookDTO> getAllIssuedBooks();

}