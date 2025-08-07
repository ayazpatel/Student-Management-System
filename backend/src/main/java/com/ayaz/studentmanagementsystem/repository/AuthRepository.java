package com.ayaz.studentmanagementsystem.repository;

import com.ayaz.studentmanagementsystem.model.Auth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends JpaRepository<Auth, Integer> {
    public Auth findByUsername(String username);
}
