package com.kaushalya.web.dto;

import lombok.Data;

/** POST /api/contact — submit a contact message */
@Data
public class ContactRequest {
    private String name;
    private String email;
    private String phone;      // optional
    private String subject;
    private String message;
}
