package com.ayaz.studentmanagementsystem.service;

import com.ayaz.studentmanagementsystem.model.Student;
import com.ayaz.studentmanagementsystem.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class StudentService {
    @Autowired
    StudentRepository studentRepository;

    @Autowired
    MinioService minioService;

    public Student register(Student student, MultipartFile image) {

        // Step 2: Generate a unique object name to prevent file overwrites.
        // Example: "a1b2c3d4-e5f6-7890-g1h2-i3j4k5l6m7n8-profile.jpg"
        String uniqueObjectName = UUID.randomUUID().toString() + "-" + image.getOriginalFilename();
        String imageUrl = minioService.uploadAndGetPresignedUrl(image, uniqueObjectName);

        if (imageUrl.startsWith("http")) {
            student.setImage_object_name(uniqueObjectName);
            student.setImage_uri(imageUrl);
            return studentRepository.save(student);
        } else {
            throw new RuntimeException("Could not register student. Image upload failed with error: " + imageUrl);
        }
    }
}
