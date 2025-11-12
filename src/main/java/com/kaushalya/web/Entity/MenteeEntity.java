package com.kaushalya.web.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="mentees")
public class MenteeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String dob;
    private String pass;
    private String intrested;
    private String location;
    private String phone;
}
