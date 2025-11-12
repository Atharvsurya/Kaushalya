package com.kaushalya.web.Mentor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mentor {
    private Long id;
    private String name;
    private String email;
    private String dob;
    private String pass;
    private String skill;
    private String location;
    private String phone;
}
