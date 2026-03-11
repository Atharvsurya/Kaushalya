package com.kaushalya.web.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

/**
 * Stores messages submitted via the Contact page.
 * GET  /api/contact          → admin sees all messages
 * POST /api/contact          → public submit
 * DELETE /api/contact/{id}   → admin delete
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "contact_message")
public class ContactMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    private String phone;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false, length = 2000)
    private String message;

    /** PENDING | READ */
    @Column(nullable = false)
    private String status = "PENDING";

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
