package com.kaushalya.web.Service;

import com.kaushalya.web.Mentee.Mentee;

import java.util.List;

public interface MenteeService {
    List<Mentee> readMentees();
    String createMentee(Mentee mentee);
    boolean deleteMentee(Long id);
}
