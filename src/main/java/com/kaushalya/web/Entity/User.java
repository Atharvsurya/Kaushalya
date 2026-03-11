package com.kaushalya.web.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Single "user" table for the entire Kaushalya platform.
 * role = STUDENT | MENTOR | ADMIN
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "app_users")
public class  User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;   // BCrypt hashed

    /** STUDENT, MENTOR, ADMIN */
    @Column(nullable = false)
    private String role;

    /** skills the mentor teaches / student wants to learn */
    private String skills;

    private String phone;

    private String location;

    /** stored as plain string e.g. "12/12/2001" */
    private String dob;

    /** token for forgot-password reset flow */
    private String resetToken;
}
