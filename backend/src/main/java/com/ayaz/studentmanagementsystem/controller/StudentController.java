package com.ayaz.studentmanagementsystem.controller;

import com.ayaz.studentmanagementsystem.exception.FileUploadException;
import com.ayaz.studentmanagementsystem.model.Student;
import com.ayaz.studentmanagementsystem.model.enums.Gender;
import com.ayaz.studentmanagementsystem.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    StudentService studentService;


//    @PreAuthorize("hasAnyRole('TEACHER','STUDENT')")
//    @PostMapping("/register")
//    public ResponseEntity<?> register(
//            @RequestPart(value = "student") @Valid Student student,
//            @RequestPart(value = "image") MultipartFile image
//    ) throws FileUploadException {
//        Student registeredStudent = studentService.register(student, image);
//        return new ResponseEntity<>(registeredStudent, HttpStatus.CREATED);
//    }

    @PreAuthorize("hasAnyRole('TEACHER','STUDENT')")
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestParam("fullname") String fullname,
            @RequestParam("age") Integer age,
            @RequestParam("gender") Gender gender,
            @RequestParam("course") String course,
            @RequestParam("address") String address,
            @RequestPart(value = "image") MultipartFile image
    ) throws FileUploadException {
        Student student = new Student();
        student.setFullname(fullname);
        student.setAge(age);
        student.setGender(gender);
        student.setCourse(course);
        student.setAddress(address);

        Student registeredStudent = studentService.register(student, image);
        return new ResponseEntity<>(registeredStudent, HttpStatus.CREATED);
    }

    /*
    @PreAuthorize("hasAnyRole('TEACHER','STUDENT')")
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestPart(value = "student") @Valid Student student,
            @RequestPart(value = "image") MultipartFile image
    ) throws FileUploadException {
        Student registeredStudent = studentService.register(student, image);
        return new ResponseEntity<>(registeredStudent, HttpStatus.CREATED);
    }
     */

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Student> updateStudent(
            @PathVariable Integer id,
            @RequestPart("student") @Valid Student studentDetails,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) throws FileUploadException {
        Student updatedStudent = studentService.update(id, studentDetails, image);
        return ResponseEntity.ok(updatedStudent);
    }

    @GetMapping("/view/{id}")
    @PreAuthorize("hasAnyRole('TEACHER', 'STUDENT')")
    public ResponseEntity<Student> getStudentById(@PathVariable Integer id) {
        return studentService.getStudentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/view-all")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Void> deleteStudent(@PathVariable Integer id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build(); // HTTP 204 No Content is standard for successful deletion
    }
}
