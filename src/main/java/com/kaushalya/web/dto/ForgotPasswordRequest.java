package com.kaushalya.web.dto;

import lombok.Data;

/** Payload sent by forgot.html → POST /api/auth/forgot-password */
@Data
public class ForgotPasswordRequest {
    private String email;
}
