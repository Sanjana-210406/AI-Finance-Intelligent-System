package com.javeriya.aifinance;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/screenshot")
public class ScreenshotController {

    @Value("${app.upload.dir}")
    private String uploadDir;

    // Accept image upload and store temporarily
    @PostMapping("/upload/{userId}")
    public ResponseEntity<?> uploadScreenshot(
            @PathVariable Long userId,
            @RequestParam("file") MultipartFile file) {

        try {
            // Validate file type
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.badRequest().body("Only image files are allowed");
            }

            // Create upload directory if not exists
            File uploadFolder = new File(uploadDir);
            if (!uploadFolder.exists()) {
                uploadFolder.mkdirs();
            }

            // Generate unique filename
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null && originalFilename.contains(".")
                    ? originalFilename.substring(originalFilename.lastIndexOf("."))
                    : ".jpg";
            String uniqueFilename = "user_" + userId + "_" + UUID.randomUUID() + extension;

            // Save file temporarily
            Path filePath = Paths.get(uploadDir, uniqueFilename);
            Files.write(filePath, file.getBytes());

            // Return file path for OCR processing
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Screenshot uploaded successfully");
            response.put("filePath", filePath.toString());
            response.put("fileName", uniqueFilename);
            response.put("userId", userId);
            response.put("status", "READY_FOR_OCR");

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body("Failed to upload file: " + e.getMessage());
        }
    }

    // Delete file after OCR processing
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteFile(@RequestParam String fileName) {
        try {
            Path filePath = Paths.get(uploadDir, fileName);
            Files.deleteIfExists(filePath);

            Map<String, String> response = new HashMap<>();
            response.put("message", "File deleted successfully");
            response.put("fileName", fileName);

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body("Failed to delete file: " + e.getMessage());
        }
    }

    // Get upload status
    @GetMapping("/status/{fileName}")
    public ResponseEntity<?> getFileStatus(@PathVariable String fileName) {
        Path filePath = Paths.get(uploadDir, fileName);
        boolean exists = Files.exists(filePath);

        Map<String, Object> response = new HashMap<>();
        response.put("fileName", fileName);
        response.put("exists", exists);
        response.put("status", exists ? "AVAILABLE" : "NOT_FOUND");

        return ResponseEntity.ok(response);
    }
}