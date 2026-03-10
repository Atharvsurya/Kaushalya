package com.kaushalya.web.dto;

import lombok.Data;

/** Payload sent by login.html → POST /api/auth/login */
@Data
public class LoginRequest {
    private String email;
    private String password;
}
