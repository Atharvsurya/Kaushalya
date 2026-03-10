package com.kaushalya.web.controller;

import com.kaushalya.web.dto.*;
import com.kaushalya.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Admin Dashboard endpoints — used by admin.html
 *
 *  GET  /api/admin/stats          → dashboard cards (Total Users, Mentors, Students)
 *  GET  /api/admin/users          → User Management table (all users)
 *  GET  /api/admin/users/role/{role} → filter by STUDENT | MENTOR
 *  DELETE /api/admin/users/{id}   → delete a user
 */
@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private UserService userService;

    /** admin.html → dashboard stat cards */
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getDashboardStats() {
        return ResponseEntity.ok(userService.getDashboardStats());
    }

    /** admin.html → User Management — full user list */
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /** admin.html → filter users by role */
    @GetMapping("/users/role/{role}")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getUsersByRole(@PathVariable String role) {
        return ResponseEntity.ok(userService.getUsersByRole(role));
    }

    /** admin.html → delete user from User Management table */
    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        ApiResponse<Void> res = userService.deleteUser(id);
        return res.isSuccess()
                ? ResponseEntity.ok(res)
                : ResponseEntity.status(404).body(res);
    }
}
