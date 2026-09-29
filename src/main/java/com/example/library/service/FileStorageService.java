package com.example.library.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorageService {

    /**
     * Stores a student profile image.
     *
     * @param file Uploaded image
     * @return Relative image path
     */
    String storeStudentImage(MultipartFile file) throws IOException;

    /**
     * Stores a librarian profile image.
     *
     * @param file Uploaded image
     * @return Relative image path
     */
    String storeLibrarianImage(MultipartFile file) throws IOException;

    /**
     * Stores a book cover image.
     *
     * @param file Uploaded image
     * @return Relative image path
     */
    String storeBookImage(MultipartFile file) throws IOException;

    /**
     * Deletes an existing file.
     *
     * @param filePath Relative path
     * @return true if deleted successfully
     */
    boolean deleteFile(String filePath) throws IOException;

}