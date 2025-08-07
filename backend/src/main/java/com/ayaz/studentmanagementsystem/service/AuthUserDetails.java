package com.ayaz.studentmanagementsystem.service;

import com.ayaz.studentmanagementsystem.model.Auth;
import com.ayaz.studentmanagementsystem.model.AuthPrincipal;
import com.ayaz.studentmanagementsystem.repository.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthUserDetails implements UserDetailsService {
    @Autowired
    AuthRepository authRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Auth user = authRepository.findByUsername(username);
        if (user == null) {
            System.out.println("User not found with username: " + username);
            throw  new UsernameNotFoundException("User not found with username: " + username);
        } else {
            return new AuthPrincipal(user);
        }
    }
}
