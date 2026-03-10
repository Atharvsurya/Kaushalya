  package com.kaushalya.web.dto;

import lombok.Data;

/**
 * Payload for PATCH /api/auth/profile/{id}
 *
 * All fields are optional — only non-null values are applied,
 * so the frontend can send just the fields it wants to change.
 *
 * Password change is handled separately via currentPassword + newPassword.
 * Leave both null to skip the password update.
 */
@Data
public class UpdateProfileRequest {

    /** Display name */
    private String name;

    /** New email address (checked for uniqueness) */
    private String email;

    /**
     * Role change: "STUDENT" | "MENTOR"
     * Restrict this endpoint to ADMIN callers in your security config.
     */
    private String role;

    /** Comma-separated skill tags, e.g. "Java,Spring,React" */
    private String skills;

    /** Contact phone number */
    private String phone;

    /** City / country free-text */
    private String location;

    /** Date of birth — stored as String, e.g. "1999-07-15" */
    private String dob;

    // ── Password change (both required together, or both omitted) ──

    /** Must match the user's current stored password */
    private String currentPassword;

    /** Minimum 6 characters; replaces the current password */
    private String newPassword;
}
