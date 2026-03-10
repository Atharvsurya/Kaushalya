package com.kaushalya.web.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.kaushalya.web.dto.ApiResponse;
import com.kaushalya.web.dto.ForgotPasswordRequest;
import com.kaushalya.web.dto.LoginRequest;
import com.kaushalya.web.dto.RegisterRequest;
import com.kaushalya.web.dto.ResetPasswordRequest;
import com.kaushalya.web.dto.UpdateProfileRequest;
import com.kaushalya.web.dto.UserResponse;
import com.kaushalya.web.entity.User;
import com.kaushalya.web.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // ─────────────────────────────────────────────
    // REGISTER  (signup.html)
    // ─────────────────────────────────────────────
    public ApiResponse<UserResponse> register(RegisterRequest req) {

        if (req.getName() == null || req.getName().isBlank())
            return ApiResponse.fail("Name is required.");

        if (req.getEmail() == null || req.getEmail().isBlank())
            return ApiResponse.fail("Email is required.");

        if (req.getPassword() == null || req.getPassword().length() < 6)
            return ApiResponse.fail("Password must be at least 6 characters.");

        if (!req.getPassword().equals(req.getConfirmPassword()))
            return ApiResponse.fail("Passwords do not match.");

        if (userRepository.existsByEmail(req.getEmail()))
            return ApiResponse.fail("An account with this email already exists.");

        User user = new User();
        user.setName(req.getName());
        user.setEmail(req.getEmail().toLowerCase().trim());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        // Default role is STUDENT; frontend can send MENTOR
        user.setRole(req.getRole() != null && req.getRole().equalsIgnoreCase("MENTOR")
                     ? "MENTOR" : "STUDENT");

        userRepository.save(user);
        return ApiResponse.ok("Account created successfully!", toResponse(user));
    }

    // ─────────────────────────────────────────────
    // LOGIN  (login.html)
    // ─────────────────────────────────────────────
    public ApiResponse<UserResponse> login(LoginRequest req) {

        if (req.getEmail() == null || req.getPassword() == null)
            return ApiResponse.fail("Email and password are required.");

        Optional<User> opt = userRepository.findByEmail(req.getEmail().toLowerCase().trim());

        if (opt.isEmpty())
            return ApiResponse.fail("No account found with this email.");

        User user = opt.get();

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword()))
            return ApiResponse.fail("Invalid password.");

        return ApiResponse.ok("Login successful!", toResponse(user));
    }

    // ─────────────────────────────────────────────
    // FORGOT PASSWORD  (forgot.html)
    // ─────────────────────────────────────────────
    public ApiResponse<Void> forgotPassword(ForgotPasswordRequest req) {

        Optional<User> opt = userRepository.findByEmail(req.getEmail().toLowerCase().trim());

        if (opt.isEmpty())
            return ApiResponse.fail("No account found with that email.");

        String token = UUID.randomUUID().toString();
        User user = opt.get();
        user.setResetToken(token);
        userRepository.save(user);

        // In a real app you would send an email here.
        // For now the token is returned in the response so you can test it.
        System.out.println(">>> PASSWORD RESET TOKEN for " + user.getEmail() + ": " + token);

        return ApiResponse.ok("Password reset link sent to your email! (check server console for token)", null);
    }

    // ─────────────────────────────────────────────
    // RESET PASSWORD  (via token)
    // ─────────────────────────────────────────────
    public ApiResponse<Void> resetPassword(ResetPasswordRequest req) {

        if (req.getNewPassword() == null || req.getNewPassword().length() < 6)
            return ApiResponse.fail("Password must be at least 6 characters.");

        Optional<User> opt = userRepository.findByResetToken(req.getToken());

        if (opt.isEmpty())
            return ApiResponse.fail("Invalid or expired reset token.");

        User user = opt.get();
        user.setPassword(passwordEncoder.encode(req.getNewPassword()));
        user.setResetToken(null);   // invalidate token after use
        userRepository.save(user);

        return ApiResponse.ok("Password reset successfully. You can now log in.", null);
    }

    // ─────────────────────────────────────────────
    // GET USER PROFILE  (music.html profile menu)
    // ─────────────────────────────────────────────
    public ApiResponse<UserResponse> getProfile(String email) {
        return userRepository.findByEmail(email).map(u -> ApiResponse.ok("User found.", toResponse(u))).orElse(ApiResponse.fail("User not found."));
    }

    // ─────────────────────────────────────────────
    // UPDATE USER PROFILE  PATCH /api/auth/profile/{id}
    // ─────────────────────────────────────────────
    /**
     * Partial update — only non-null fields in the request are written.
     * Password change requires {@code currentPassword} to be supplied and
     * verified before {@code newPassword} is applied.
     */
    public ApiResponse<UserResponse> updateProfile(String email, UpdateProfileRequest req) {

        Optional<User> opt = userRepository.findByEmail(email); 
        if (opt.isEmpty())
            return ApiResponse.fail("User not found.");

        User user = opt.get();

        // ── name ──────────────────────────────────────
        if (req.getName() != null) {
            if (req.getName().isBlank())
                return ApiResponse.fail("Name cannot be empty.");
            user.setName(req.getName().trim());
        }

        // ── email ─────────────────────────────────────
        if (req.getEmail() != null) {
            String newEmail = req.getEmail().toLowerCase().trim();
            if (newEmail.isBlank())
                return ApiResponse.fail("Email cannot be empty.");
            // Allow same email (no change); reject if taken by a *different* user
            if (!newEmail.equals(user.getEmail()) && userRepository.existsByEmail(newEmail))
                return ApiResponse.fail("This email is already used by another account.");
            user.setEmail(newEmail);
        }

        // ── role ──────────────────────────────────────
        // Restrict this to ADMIN in your Spring Security config if needed.
        if (req.getRole() != null) {
            String role = req.getRole().toUpperCase().trim();
            if (!role.equals("STUDENT") && !role.equals("MENTOR") && !role.equals("ADMIN"))
                return ApiResponse.fail("Role must be STUDENT, MENTOR, or ADMIN.");
            user.setRole(role);
        }

        // ── profile fields ────────────────────────────
        if (req.getSkills() != null)
            user.setSkills(req.getSkills().trim());

        if (req.getPhone() != null)
            user.setPhone(req.getPhone().trim());

        if (req.getLocation() != null)
            user.setLocation(req.getLocation().trim());

        if (req.getDob() != null)
            user.setDob(req.getDob().trim());

        // ── password change ───────────────────────────
        boolean wantsPasswordChange =
                req.getCurrentPassword() != null || req.getNewPassword() != null;

        if (wantsPasswordChange) {
            if (req.getCurrentPassword() == null || req.getCurrentPassword().isBlank())
                return ApiResponse.fail("Current password is required to set a new password.");

            if (!passwordEncoder.matches(req.getCurrentPassword(), user.getPassword()))
                return ApiResponse.fail("Current password is incorrect.");

            if (req.getNewPassword() == null || req.getNewPassword().length() < 6)
                return ApiResponse.fail("New password must be at least 6 characters.");

            if (req.getNewPassword().equals(req.getCurrentPassword()))
                return ApiResponse.fail("New password must be different from the current password.");

            user.setPassword(passwordEncoder.encode(req.getNewPassword()));
        }

        userRepository.save(user);
        return ApiResponse.ok("Profile updated successfully.", toResponse(user));
    }

    // ─────────────────────────────────────────────
    // ADMIN — get all users  (admin.html)
    // ─────────────────────────────────────────────
    public ApiResponse<List<UserResponse>> getAllUsers() {
        List<UserResponse> list = userRepository.findAll()
                .stream().map(this::toResponse).collect(Collectors.toList());
        return ApiResponse.ok("Users fetched.", list);
    }

    public ApiResponse<List<UserResponse>> getUsersByRole(String role) {
        List<UserResponse> list = userRepository.findAllByRole(role.toUpperCase())
                .stream().map(this::toResponse).collect(Collectors.toList());
        return ApiResponse.ok(role + "s fetched.", list);
    }

    // ─────────────────────────────────────────────
    // ADMIN — dashboard stats  (admin.html cards)
    // ─────────────────────────────────────────────
    public ApiResponse<Map<String, Long>> getDashboardStats() {
        long totalUsers    = userRepository.count();
        long totalMentors  = userRepository.countByRole("MENTOR");
        long totalStudents = userRepository.countByRole("STUDENT");
        return ApiResponse.ok("Stats fetched.", Map.of(
                "totalUsers",    totalUsers,
                "totalMentors",  totalMentors,
                "totalStudents", totalStudents
        ));
    }

    // ─────────────────────────────────────────────
    // ADMIN — delete user  (admin.html User Management)
    // ─────────────────────────────────────────────
    public ApiResponse<Void> deleteUser(Long id) {
        if (!userRepository.existsById(id))
            return ApiResponse.fail("User not found.");
        userRepository.deleteById(id);
        return ApiResponse.ok("User deleted.", null);
    }

    // ─────────────────────────────────────────────
    // MENTOR PANEL — get all students enrolled
    //  (panel.html Students Panel) filtered by mentorId stored in session
    // ─────────────────────────────────────────────
    public ApiResponse<List<UserResponse>> getAllStudents() {
        return getUsersByRole("STUDENT");
    }

    // ─────────────────────────────────────────────
    // Utility: entity → safe DTO
    // ─────────────────────────────────────────────
    private UserResponse toResponse(User u) {
        return new UserResponse(
            u.getId(), u.getName(), u.getEmail(),
            u.getRole(), u.getSkills(), u.getPhone(),
            u.getLocation(), u.getDob()
        );
    }
}