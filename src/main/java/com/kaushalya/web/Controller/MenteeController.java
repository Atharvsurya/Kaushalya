package com.kaushalya.web.Controller;

import com.kaushalya.web.Mentee.Mentee;
import com.kaushalya.web.Service.MenteeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MenteeController {
    @Autowired
    MenteeService menteeService;

    @GetMapping("mentees")
    public List<Mentee> getAllMentees(){
        return menteeService.readMentees();
    }
    @PostMapping("mentees")
    public String createMentee(@RequestBody Mentee mentee){
        return menteeService.createMentee(mentee);
    }
    @DeleteMapping("mentees/{id}")
    public boolean deleteMentee(@PathVariable Long id){
        if(menteeService.deleteMentee(id))
            return true;
        return false;
    }
}
