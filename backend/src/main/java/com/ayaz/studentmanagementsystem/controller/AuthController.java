package com.ayaz.studentmanagementsystem.controller;

import com.ayaz.studentmanagementsystem.model.Auth;
import com.ayaz.studentmanagementsystem.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Auth> registerUser(@RequestBody Auth auth) {
        if(auth == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Auth auth1 = authService.register(auth);
        if (auth1.getUsername().equals(auth.getUsername())) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/login")
    public ResponseEntity<Auth> loginUser(@RequestBody Auth auth) {
        if(auth == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
