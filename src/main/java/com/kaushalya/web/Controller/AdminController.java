package com.kaushalya.web.controller;

import com.kaushalya.web.dto.*;
import com.kaushalya.web.service.UserService;
import com.kaushalya.web.repository.ContactMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Admin Dashboard endpoints — admin.html
 *
 *  GET    /api/admin/stats             → dashboard cards (Users, Mentors, Students, Admins, Pending Messages)
 *  GET    /api/admin/users             → all users
 *  GET    /api/admin/users/role/{role} → filter STUDENT | MENTOR | ADMIN
 *  DELETE /api/admin/users/{id}        → delete user
 */
@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private ContactMessageRepository contactMessageRepository;

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getDashboardStats() {
        return ResponseEntity.ok(userService.getDashboardStats(contactMessageRepository));
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/users/role/{role}")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getUsersByRole(@PathVariable String role) {
        return ResponseEntity.ok(userService.getUsersByRole(role));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        ApiResponse<Void> res = userService.deleteUser(id);
        return res.isSuccess()
                ? ResponseEntity.ok(res)
                : ResponseEntity.status(404).body(res);
    }
}