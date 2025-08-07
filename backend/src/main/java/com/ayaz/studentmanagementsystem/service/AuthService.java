package com.ayaz.studentmanagementsystem.service;

import com.ayaz.studentmanagementsystem.model.Auth;
import com.ayaz.studentmanagementsystem.repository.AuthRepository;
import io.jsonwebtoken.Jwt;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    JwtService jwtService;

    @Autowired
    AuthRepository authRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    public Auth register(Auth auth) {
        auth.setUsername(auth.getUsername().toLowerCase());
        auth.setPassword(passwordEncoder.encode(auth.getPassword()));
        authRepository.save(auth);
        return auth;
    };

    public String verify(Auth user) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(user.getUsername());
        } else {
            return "failed";
        }
    }

}
