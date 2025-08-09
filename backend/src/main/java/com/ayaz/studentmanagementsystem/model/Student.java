package com.ayaz.studentmanagementsystem.model;

import com.ayaz.studentmanagementsystem.model.enums.Gender;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "sms_student")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Full name cannot be blank")
    @Column(nullable = false)
    private String fullname;

    @NotNull(message = "Age cannot be null")
    @Min(value = 3, message = "Age must be greater than or equal to 3")
    @Column(nullable = false)
    private Integer age;

    @NotNull(message = "Gender must be specified")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @NotBlank(message = "Course cannot be blank")
    @Column(nullable = false)
    private String course;

    @NotBlank(message = "Address cannot be blank")
    @Column(nullable = false)
    private String address;

    private String image_object_name;

    @Transient // This tells JPA to not map this field to a db column
    private String image_uri;

    public Student() {}
//
//    public Student(Integer id, String fullname, Integer age, String gender, String course, String address, String image_uri) {
//        this.fullname = fullname;
//        this.age = age;
//        this.gender = gender;
//        this.course = course;
//        this.address = address;
//        this.image_uri = image_uri;
//    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage_object_name() {
        return image_object_name;
    }

    public void setImage_object_name(String image_object_name) {
        this.image_object_name = image_object_name;
    }

    public String getImage_uri() {
        return image_uri;
    }

    public void setImage_uri(String image_uri) {
        this.image_uri = image_uri;
    }
}

