package com.kaushalya.web.controller;

import com.kaushalya.web.dto.*;
import com.kaushalya.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Mentor Panel endpoints:
 *
 *  GET /api/mentor/students       → Mentor Panel — all students
 *  GET /api/mentor/all            → home.html / mentors.html — all mentors
 *  GET /api/mentor/{id}           → mentor-detail.html — single mentor by ID
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

    /** home.html / mentors.html → list of all mentors */
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllMentors() {
        return ResponseEntity.ok(userService.getUsersByRole("MENTOR"));
    }

    /**
     * mentor-detail.html → fetch a single mentor by their user ID.
     * Returns 404 if ID does not belong to a MENTOR role user.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getMentorById(@PathVariable Long id) {
        ApiResponse<UserResponse> res = userService.getMentorById(id);
        return res.isSuccess()
                ? ResponseEntity.ok(res)
                : ResponseEntity.status(404).body(res);
    }
}