package com.ayaz.studentmanagementsystem.service;

import com.ayaz.studentmanagementsystem.exception.FileUploadException;
import com.ayaz.studentmanagementsystem.exception.ResourceNotFoundException;
import com.ayaz.studentmanagementsystem.model.Student;
import com.ayaz.studentmanagementsystem.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StudentService {
    @Autowired
    StudentRepository studentRepository;

    @Autowired
    MinioService minioService;

    public Student register(Student student, MultipartFile image) throws FileUploadException  {

        // Step 2: Generate a unique object name to prevent file overwrites.
        // Example: "a1b2c3d4-e5f6-7890-g1h2-i3j4k5l6m7n8-profile.jpg"
        String uniqueObjectName = UUID.randomUUID().toString() + "-" + image.getOriginalFilename();
        minioService.uploadFile(image, uniqueObjectName);
        student.setImage_object_name(uniqueObjectName);

        return studentRepository.save(student);
    }

    public Student update(Integer id, Student studentDetails, MultipartFile image) throws FileUploadException  {
        Student existingStudent = studentRepository
                .findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Student not found with this id"));

        existingStudent.setFullname(studentDetails.getFullname());
        existingStudent.setAge(studentDetails.getAge());
        existingStudent.setGender(studentDetails.getGender());
        existingStudent.setCourse(studentDetails.getCourse());
        existingStudent.setAddress(studentDetails.getAddress());

        if (image != null && !image.isEmpty()) {
            if (existingStudent.getImage_object_name() != null && !existingStudent.getImage_object_name().isEmpty()) {
                minioService.deleteFile(existingStudent.getImage_object_name());
            }
            String newImageObjectName = UUID.randomUUID().toString() + "-" + image.getOriginalFilename();
            minioService.uploadFile(image, newImageObjectName);
            existingStudent.setImage_object_name(newImageObjectName);
        }
        return studentRepository.save(existingStudent);
    }

    public Optional<Student> getStudentById(Integer id) {
        return  studentRepository.findById(id).map(this::attachPresignedUrl);
    }

    public List<Student> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        students.forEach(this::attachPresignedUrl); // Attach URL to each student
        return students;
    }

    public void deleteStudent(Integer id) {
        Student studentToDelete = studentRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot delete. Student not found wth id: " + id));

        if (studentToDelete.getImage_object_name() != null && !studentToDelete.getImage_object_name().isEmpty()) {
            minioService.deleteFile(studentToDelete.getImage_object_name());
        }
        studentRepository.delete(studentToDelete);
    }

    private Student attachPresignedUrl(Student student) {
        if (student.getImage_object_name() != null) {
            student.setImage_uri(minioService.getPresignedUrl(student.getImage_object_name()));
        }
        return student;
    }
}
