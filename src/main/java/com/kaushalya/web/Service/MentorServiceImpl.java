package com.kaushalya.web.Service;

import com.kaushalya.web.Entity.MentorEntity;
import com.kaushalya.web.Mentor.Mentor;
import com.kaushalya.web.Repository.MentorRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MentorServiceImpl implements MentorService{
    @Autowired
    MentorRepository mentorRepository;

    @Override
    public String createMentor(Mentor mentor) {
        MentorEntity entity = new MentorEntity();
        BeanUtils.copyProperties(mentor,entity);
        mentorRepository.save(entity);
            return "Saved Successfully!!";
    }

    @Override
    public List<Mentor> readMentors() {
        List<MentorEntity> ent = mentorRepository.findAll();
        List<Mentor> mentors = new ArrayList<>();

        for (MentorEntity entity : ent){
            Mentor m = new Mentor();
            BeanUtils.copyProperties(entity,m);
            mentors.add(m);
        }
        return mentors;
    }

    @Override
    public boolean deleteMentor(Long id) {
        if (mentorRepository.existsById(id)) {
            mentorRepository.deleteById(id);
            return true;
        }
        else{
            return false;
        }
    }

    public Mentor findByEmail(String email) {
        // 1. Get the Entity from the DB
        MentorEntity entity = mentorRepository.findByEmail(email);

        if (entity == null) return null;

        // 2. Map Entity to DTO (Mentor)
        Mentor mentor = new Mentor();
        mentor.setEmail(entity.getEmail());
        mentor.setPass(entity.getPass()); // This is where getPass() is called
        mentor.setName(entity.getName());
        // ... map other fields

        return mentor;
    }
}
