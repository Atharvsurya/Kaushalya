package com.kaushalya.web.Controller;

import com.kaushalya.web.Mentor.Mentor;
import com.kaushalya.web.Service.MentorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
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
    @PostMapping("/login")
    public ResponseEntity<?> login (@RequestBody Mentor loginData) {
        Mentor mentor = mentorService.findByEmail(loginData.getEmail());

        if (mentor != null && mentor.getPass().equals(loginData.getPass())) {
            return ResponseEntity.ok(mentor);
        } else {
            return ResponseEntity.status(401).body("{\"message\": \"Invalid email or password\"}");
        }
    }
}