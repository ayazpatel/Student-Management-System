package com.ayaz.studentmanagementsystem.controller;

import com.ayaz.studentmanagementsystem.model.Auth;
import com.ayaz.studentmanagementsystem.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth/")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Auth> registerUser(@RequestBody @Valid Auth auth) {
        // @RequestBody handles the null check. If the body is empty, Spring returns a 400 Bad Request.
        // @Valid will trigger validation rules on the Auth entity if you add them.
        Auth registeredUser = authService.register(auth);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Auth auth) {
        try {
            String token = authService.verify(auth);
            // The verify method will throw an AuthenticationException on failure, which is caught below.
            return ResponseEntity.ok(Map.of("token", token));
        } catch (Exception e) {
            // Let the GlobalExceptionHandler handle this in the future, but for now, this is fine.
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
