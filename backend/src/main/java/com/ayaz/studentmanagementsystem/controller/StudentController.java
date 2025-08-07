package com.ayaz.studentmanagementsystem.controller;

import com.ayaz.studentmanagementsystem.model.Student;
import com.ayaz.studentmanagementsystem.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    StudentService studentService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(
            @RequestPart(value = "student", required = false) Student student,
            @RequestPart(value = "image", required = false) MultipartFile image
            ) {
        try {
            if (student == null) {
                return new ResponseEntity<>("Student data is missing", HttpStatus.BAD_REQUEST);
            }
            if (image == null) {
                return new ResponseEntity<>("Image data is missing", HttpStatus.BAD_REQUEST);
            }
            Student student1 = studentService.register(student, image);
            return new ResponseEntity<>(student1, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
