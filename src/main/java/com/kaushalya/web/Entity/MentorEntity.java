package com.kaushalya.web.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="mentors")
public class MentorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String dob;
    private String pass;
    private String skill;
    private String location;
    private String phone;
}
