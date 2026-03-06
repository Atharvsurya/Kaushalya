package com.kaushalya.web.Service;

import com.kaushalya.web.Mentor.Mentor;

import java.util.List;

public interface MentorService {
    String createMentor(Mentor mentor);
    List<Mentor> readMentors();
    boolean deleteMentor(Long id);
    Mentor findByEmail(String email);
}
