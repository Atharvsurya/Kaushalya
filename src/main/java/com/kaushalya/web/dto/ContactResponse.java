package com.kaushalya.web.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

/** Safe contact message DTO returned to admin */
@Data
@AllArgsConstructor
public class ContactResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String subject;
    private String message;
    private String status;
    private LocalDateTime createdAt;
}
