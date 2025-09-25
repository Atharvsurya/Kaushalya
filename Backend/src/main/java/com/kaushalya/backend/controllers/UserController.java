package com.kaushalya.backend.controllers;

import org.springframework.web.bind.annotation.*;
import com.kaushalya.backend.entities.User;
import com.kaushalya.backend.repositories.UserRepository;

import java.util.List;

@RestController
@RequestMapping("/sql/users")
@CrossOrigin(origins = "*") // Allow all for testing
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
