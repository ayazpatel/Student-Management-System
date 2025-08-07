package com.ayaz.studentmanagementsystem.model;

import jakarta.persistence.*;

@Entity
@Table(name = "sms_student")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String fullname;
    private Integer age;
    private String gender;
    private String course;
    private String address;
    private String image_object_name;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
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

