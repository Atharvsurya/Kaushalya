package com.kaushalya.web.dto;

import lombok.Data;

/** Payload sent by signup.html → POST /api/auth/register */
@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private String confirmPassword;
    /** optional: STUDENT (default) or MENTOR */
    private String role;
}
