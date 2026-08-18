package com.example.library.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileStorageConfig {

    @Value("${application.upload-dir}")
    private String uploadDirectory;

    public String getUploadDirectory() {

        return uploadDirectory;

    }

}