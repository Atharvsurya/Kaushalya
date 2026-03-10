package com.kaushalya.web.controller;

import com.kaushalya.web.dto.*;
import com.kaushalya.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Mentor Panel endpoints — used by panel.html
 *
 *  GET /api/mentor/students   → Students Panel table
 *  GET /api/mentor/mentors    → list of all mentors (for home.html "Become a Mentor" section)
 */
@RestController
@RequestMapping("/api/mentor")
@CrossOrigin(origins = "*")
public class MentorController {

    @Autowired
    private UserService userService;

    /** panel.html → Students Panel section */
    @GetMapping("/students")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllStudents() {
        return ResponseEntity.ok(userService.getAllStudents());
    }

    /** home.html → list of mentors to display */
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllMentors() {
        return ResponseEntity.ok(userService.getUsersByRole("MENTOR"));
    }
}
