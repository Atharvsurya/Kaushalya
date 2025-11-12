package com.kaushalya.web.Controller;

import com.kaushalya.web.Mentor.Mentor;
import com.kaushalya.web.Service.MentorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MentorController {
    @Autowired
    MentorService mentorService;

    @GetMapping("mentors")
    public List<Mentor> getAllMentors(){
        return mentorService.readMentors();
    }
    @PostMapping("mentors")
    public String createMentor(@RequestBody Mentor mentor){
        return mentorService.createMentor(mentor);
    }
    @DeleteMapping("mentors/{id}")
    public boolean deleteMentor(@PathVariable Long id){
        if(mentorService.deleteMentor(id))
            return true;
        else
            return false;
    }
}