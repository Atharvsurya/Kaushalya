package com.kaushalya.web.service;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.kaushalya.web.dto.*;
import com.kaushalya.web.entity.User;
import com.kaushalya.web.repository.ContactMessageRepository;
import com.kaushalya.web.repository.UserRepository;

@Service
public class UserService {

    @Autowired private UserRepository userRepository;
    @Autowired private BCryptPasswordEncoder passwordEncoder;

    // ── REGISTER ──────────────────────────────────────────────────────────────
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
        user.setName(req.getName().trim());
        user.setEmail(req.getEmail().toLowerCase().trim());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setRole(req.getRole() != null && req.getRole().equalsIgnoreCase("MENTOR") ? "MENTOR" : "STUDENT");
        userRepository.save(user);
        return ApiResponse.ok("Account created successfully!", toResponse(user));
    }

    // ── LOGIN ─────────────────────────────────────────────────────────────────
    public ApiResponse<UserResponse> login(LoginRequest req) {
        if (req.getEmail() == null || req.getPassword() == null)
            return ApiResponse.fail("Email and password are required.");
        Optional<User> opt = userRepository.findByEmail(req.getEmail().toLowerCase().trim());
        if (opt.isEmpty()) return ApiResponse.fail("No account found with this email.");
        User user = opt.get();
        if (!passwordEncoder.matches(req.getPassword(), user.getPassword()))
            return ApiResponse.fail("Invalid password.");
        return ApiResponse.ok("Login successful!", toResponse(user));
    }

    // ── FORGOT PASSWORD ───────────────────────────────────────────────────────
    public ApiResponse<Void> forgotPassword(ForgotPasswordRequest req) {
        Optional<User> opt = userRepository.findByEmail(req.getEmail().toLowerCase().trim());
        if (opt.isEmpty()) return ApiResponse.fail("No account found with that email.");
        String token = UUID.randomUUID().toString();
        User user = opt.get();
        user.setResetToken(token);
        userRepository.save(user);
        System.out.println(">>> PASSWORD RESET TOKEN for " + user.getEmail() + ": " + token);
        return ApiResponse.ok("Reset token generated! Check the server console for the token.", null);
    }

    // ── RESET PASSWORD ────────────────────────────────────────────────────────
    public ApiResponse<Void> resetPassword(ResetPasswordRequest req) {
        if (req.getNewPassword() == null || req.getNewPassword().length() < 6)
            return ApiResponse.fail("Password must be at least 6 characters.");
        Optional<User> opt = userRepository.findByResetToken(req.getToken());
        if (opt.isEmpty()) return ApiResponse.fail("Invalid or expired reset token.");
        User user = opt.get();
        user.setPassword(passwordEncoder.encode(req.getNewPassword()));
        user.setResetToken(null);
        userRepository.save(user);
        return ApiResponse.ok("Password reset successfully. You can now log in.", null);
    }

    // ── GET PROFILE ───────────────────────────────────────────────────────────
    public ApiResponse<UserResponse> getProfile(String email) {
        return userRepository.findByEmail(email)
                .map(u -> ApiResponse.ok("User found.", toResponse(u)))
                .orElse(ApiResponse.fail("User not found."));
    }

    // ── GET MENTOR BY ID ──────────────────────────────────────────────────────
    /** Used by mentor-detail.html — only returns the user if their role is MENTOR */
    public ApiResponse<UserResponse> getMentorById(Long id) {
        Optional<User> opt = userRepository.findById(id);
        if (opt.isEmpty()) return ApiResponse.fail("Mentor not found.");
        User user = opt.get();
        if (!"MENTOR".equals(user.getRole()))
            return ApiResponse.fail("This user is not a mentor.");
        return ApiResponse.ok("Mentor found.", toResponse(user));
    }

    // ── UPDATE PROFILE ────────────────────────────────────────────────────────
    public ApiResponse<UserResponse> updateProfile(String email, UpdateProfileRequest req) {
        Optional<User> opt = userRepository.findByEmail(email);
        if (opt.isEmpty()) return ApiResponse.fail("User not found.");
        User user = opt.get();

        if (req.getName() != null) {
            if (req.getName().isBlank()) return ApiResponse.fail("Name cannot be empty.");
            user.setName(req.getName().trim());
        }
        if (req.getEmail() != null) {
            String newEmail = req.getEmail().toLowerCase().trim();
            if (newEmail.isBlank()) return ApiResponse.fail("Email cannot be empty.");
            if (!newEmail.equals(user.getEmail()) && userRepository.existsByEmail(newEmail))
                return ApiResponse.fail("This email is already used by another account.");
            user.setEmail(newEmail);
        }
        if (req.getRole() != null) {
            String role = req.getRole().toUpperCase().trim();
            if (!role.equals("STUDENT") && !role.equals("MENTOR") && !role.equals("ADMIN"))
                return ApiResponse.fail("Role must be STUDENT, MENTOR, or ADMIN.");
            user.setRole(role);
        }
        if (req.getSkills() != null)   user.setSkills(req.getSkills().trim());
        if (req.getPhone() != null)    user.setPhone(req.getPhone().trim());
        if (req.getLocation() != null) user.setLocation(req.getLocation().trim());
        if (req.getDob() != null)      user.setDob(req.getDob().trim());

        boolean wantsPasswordChange = req.getCurrentPassword() != null || req.getNewPassword() != null;
        if (wantsPasswordChange) {
            if (req.getCurrentPassword() == null || req.getCurrentPassword().isBlank())
                return ApiResponse.fail("Current password is required to set a new password.");
            if (!passwordEncoder.matches(req.getCurrentPassword(), user.getPassword()))
                return ApiResponse.fail("Current password is incorrect.");
            if (req.getNewPassword() == null || req.getNewPassword().length() < 6)
                return ApiResponse.fail("New password must be at least 6 characters.");
            if (req.getNewPassword().equals(req.getCurrentPassword()))
                return ApiResponse.fail("New password must differ from current password.");
            user.setPassword(passwordEncoder.encode(req.getNewPassword()));
        }

        userRepository.save(user);
        return ApiResponse.ok("Profile updated successfully.", toResponse(user));
    }

    // ── ADMIN ─────────────────────────────────────────────────────────────────
    public ApiResponse<List<UserResponse>> getAllUsers() {
        return ApiResponse.ok("Users fetched.",
            userRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList()));
    }

    public ApiResponse<List<UserResponse>> getUsersByRole(String role) {
        return ApiResponse.ok(role + "s fetched.",
            userRepository.findAllByRole(role.toUpperCase()).stream().map(this::toResponse).collect(Collectors.toList()));
    }

    /** Now includes totalAdmins and pendingMessages counts */
    public ApiResponse<Map<String, Long>> getDashboardStats(ContactMessageRepository contactRepo) {
        Map<String, Long> stats = new HashMap<>();
        stats.put("totalUsers",       userRepository.count());
        stats.put("totalMentors",     userRepository.countByRole("MENTOR"));
        stats.put("totalStudents",    userRepository.countByRole("STUDENT"));
        stats.put("totalAdmins",      userRepository.countByRole("ADMIN"));
        stats.put("pendingMessages",  contactRepo.countByStatus("PENDING"));
        return ApiResponse.ok("Stats fetched.", stats);
    }

    public ApiResponse<Void> deleteUser(Long id) {
        if (!userRepository.existsById(id)) return ApiResponse.fail("User not found.");
        userRepository.deleteById(id);
        return ApiResponse.ok("User deleted.", null);
    }

    // ── MENTOR PANEL ──────────────────────────────────────────────────────────
    public ApiResponse<List<UserResponse>> getAllStudents() {
        return getUsersByRole("STUDENT");
    }

    // ── UTILITY ───────────────────────────────────────────────────────────────
    private UserResponse toResponse(User u) {
        return new UserResponse(
            u.getId(), u.getName(), u.getEmail(),
            u.getRole(), u.getSkills(), u.getPhone(),
            u.getLocation(), u.getDob()
        );
    }
}