package com.ayaz.studentmanagementsystem.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @RequestMapping("/")
    public String home(HttpServletRequest httpServletRequest) {
        return "Session ID: " + httpServletRequest.getSession().getId();
    }
}
