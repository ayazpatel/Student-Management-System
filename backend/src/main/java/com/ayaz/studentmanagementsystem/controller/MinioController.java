package com.ayaz.studentmanagementsystem.controller;

import com.ayaz.studentmanagementsystem.service.MinioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;

@RestController
public class MinioController {

    @Autowired
    private MinioService minioService;

    @PostMapping("/upload-and-get-url")
    public ResponseEntity<?> uploadFileAndGetUrl(@RequestParam("file") MultipartFile file) {
        // Using the original filename is a security risk. Generate a unique one.
        String objectName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        minioService.uploadFile(file, objectName);
        String url = minioService.getPresignedUrl(objectName);

        // A null check is safer and cleaner than startsWith("http")
        if (url != null) {
            return ResponseEntity.ok(Map.of("url", url));
        } else {
            return ResponseEntity.status(500).body(Map.of("error", "File uploaded but failed to generate URL."));
        }
    }
}
