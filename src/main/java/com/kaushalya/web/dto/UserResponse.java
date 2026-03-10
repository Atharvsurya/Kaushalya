package com.kaushalya.web.dto;

import lombok.Data;
import lombok.AllArgsConstructor;

/** Safe user data returned to frontend — never expose password/token */
@Data
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private String role;
    private String skills;
    private String phone;
    private String location;
    private String dob;
}
