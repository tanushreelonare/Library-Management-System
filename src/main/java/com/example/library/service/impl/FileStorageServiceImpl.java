package com.example.library.service.impl;

import com.example.library.service.FileStorageService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${application.upload-dir}")
    private String uploadDirectory;

    // STORE STUDENT IMAGE

    @Override
    public String storeStudentImage(MultipartFile file)
            throws IOException {

        return store(file, "students");

    }

    // STORE LIBRARIAN IMAGE

    @Override
    public String storeLibrarianImage(MultipartFile file)
            throws IOException {

        return store(file, "librarians");

    }

    // STORE BOOK IMAGE

    @Override
    public String storeBookImage(MultipartFile file)
            throws IOException {

        return store(file, "books");

    }

    // DELETE FILE

    @Override
    public boolean deleteFile(String filePath)
            throws IOException {

        if (filePath == null || filePath.isBlank()) {

            return false;

        }

        Path path = Paths.get(uploadDirectory)
                .resolve(filePath);

        return Files.deleteIfExists(path);

    }

    // COMMON STORE METHOD

    private String store(
            MultipartFile file,
            String folder
    ) throws IOException {

        if (file == null || file.isEmpty()) {

            return null;

        }

        String originalFilename =
                StringUtils.cleanPath(
                        file.getOriginalFilename()
                );

        String extension = "";

        int index = originalFilename.lastIndexOf('.');

        if (index >= 0) {

            extension = originalFilename.substring(index);

        }

        String fileName =

                LocalDateTime.now()
                        .format(
                                DateTimeFormatter.ofPattern(
                                        "yyyyMMddHHmmss"
                                )
                        )

                        + "_"

                        + UUID.randomUUID()
                        .toString()
                        .substring(0, 8)

                        + extension;

        Path uploadPath =
                Paths.get(uploadDirectory, folder);

        Files.createDirectories(uploadPath);

        Path targetLocation =
                uploadPath.resolve(fileName);

        Files.copy(
                file.getInputStream(),
                targetLocation,
                StandardCopyOption.REPLACE_EXISTING
        );

        return folder + "/" + fileName;

    }

}