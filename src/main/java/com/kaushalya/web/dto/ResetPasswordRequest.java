package com.kaushalya.web.dto;

import lombok.Data;

/** POST /api/auth/reset-password */
@Data
public class ResetPasswordRequest {
    private String token;
    private String newPassword;
}
