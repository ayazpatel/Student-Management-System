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
    private String image_uri;
}

