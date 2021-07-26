package com.example.crud.service;

import com.example.crud.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserService extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
