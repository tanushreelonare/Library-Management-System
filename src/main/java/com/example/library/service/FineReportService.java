package com.example.library.service;

import com.example.library.dto.FineReportDTO;

import java.util.List;

public interface FineReportService {

    List<FineReportDTO> getFineReports(String search, String status);
}