package com.ayaz.studentmanagementsystem.controller;

import com.ayaz.studentmanagementsystem.service.MinioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
public class MinioController {

    @Autowired
    private MinioService minioService;

    @PostMapping("/upload-and-get-url")
    public ResponseEntity<?> uploadFileAndGetUrl(@RequestParam("file") MultipartFile file) {
        String objectName = file.getOriginalFilename();
        String result = minioService.uploadAndGetPresignedUrl(file, objectName);

        // Check if the result is a URL (success) or an error message (failure)
        if (result.startsWith("http")) {
            // Success
            return ResponseEntity.ok(Map.of("url", result));
        } else {
            // Failure
            return ResponseEntity.status(500).body(Map.of("error", result));
        }
    }
}
