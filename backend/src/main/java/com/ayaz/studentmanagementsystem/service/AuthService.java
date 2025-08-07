package com.ayaz.studentmanagementsystem.service;

import com.ayaz.studentmanagementsystem.model.Auth;
import com.ayaz.studentmanagementsystem.repository.AuthRepository;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    AuthRepository authRepository;

    public Auth register(Auth auth) {
        return authRepository.save(auth);
    };

}
