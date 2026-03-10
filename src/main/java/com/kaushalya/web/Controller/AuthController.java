package com.kaushalya.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kaushalya.web.dto.ApiResponse;
import com.kaushalya.web.dto.ForgotPasswordRequest;
import com.kaushalya.web.dto.LoginRequest;
import com.kaushalya.web.dto.RegisterRequest;
import com.kaushalya.web.dto.ResetPasswordRequest;
import com.kaushalya.web.dto.UpdateProfileRequest;
import com.kaushalya.web.dto.UserResponse;
import com.kaushalya.web.service.UserService;

/**
 * Auth endpoints used by:
 *  - signup.html    → POST /api/auth/register
 *  - login.html     → POST /api/auth/login
 *  - forgot.html    → POST /api/auth/forgot-password
 *                  → POST /api/auth/reset-password
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserService userService;

    /** signup.html — register new user */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(@RequestBody RegisterRequest req) {
        ApiResponse<UserResponse> res = userService.register(req);
        return res.isSuccess()
                ? ResponseEntity.ok(res)
                : ResponseEntity.badRequest().body(res);
    }

    /** login.html — authenticate user */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserResponse>> login(@RequestBody LoginRequest req) {
        ApiResponse<UserResponse> res = userService.login(req);
        return res.isSuccess()
                ? ResponseEntity.ok(res)
                : ResponseEntity.status(401).body(res);
    }

    /** forgot.html — send reset token */
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(@RequestBody ForgotPasswordRequest req) {
        ApiResponse<Void> res = userService.forgotPassword(req);
        return res.isSuccess()
                ? ResponseEntity.ok(res)
                : ResponseEntity.badRequest().body(res);
    }

    /** reset password using token received from forgot-password */
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@RequestBody ResetPasswordRequest req) {
        ApiResponse<Void> res = userService.resetPassword(req);
        return res.isSuccess()
                ? ResponseEntity.ok(res)
                : ResponseEntity.badRequest().body(res);
    }

    /** music.html profile menu — get logged-in user data by id */
    @GetMapping("/profile/{email}")
    public ResponseEntity<ApiResponse<UserResponse>> getProfile(@PathVariable String email) {
        ApiResponse<UserResponse> res = userService.getProfile(email);
        return res.isSuccess()
                ? ResponseEntity.ok(res)
                : ResponseEntity.status(404).body(res);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // UPDATE PROFILE   PATCH /api/auth/profile/{id}
    // ─────────────────────────────────────────────────────────────────────────
    /**
     * Partial profile update.
     * Send only the fields you want to change — omitted/null fields are ignored.
     *
     * Example request body (JSON):
     * {
     *   "name":            "Alice Smith",          // optional
     *   "email":           "alice@example.com",    // optional
     *   "role":            "MENTOR",               // optional — guard with ADMIN role in security
     *   "skills":          "Java,Spring,React",    // optional
     *   "phone":           "+1 555-0100",          // optional
     *   "location":        "San Francisco, CA",    // optional
     *   "dob":             "1999-07-15",           // optional
     *   "currentPassword": "oldPass123",           // required only when changing password
     *   "newPassword":     "newPass456"            // required only when changing password
     * }
     */
    @PatchMapping("/profile/{email}")
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(
            @PathVariable String email,
            @RequestBody UpdateProfileRequest req) {

        ApiResponse<UserResponse> response = userService.updateProfile(email, req);
        int status = response.isSuccess() ? 200 : 400;
        return ResponseEntity.status(status).body(response);
    }
}
