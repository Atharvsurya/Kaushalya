package com.kaushalya.web.Service;

import com.kaushalya.web.Entity.MenteeEntity;
import com.kaushalya.web.Mentee.Mentee;
import com.kaushalya.web.Repository.MenteeRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MenteeServiceImpl implements MenteeService{
    @Autowired
    MenteeRepository menteeRepository;

    public List<Mentee> readMentees(){
        List<Mentee> mentee = new ArrayList<>();
        List<MenteeEntity> ent = menteeRepository.findAll();

        for(MenteeEntity entity: ent){
            Mentee m = new Mentee();
            BeanUtils.copyProperties(entity,m);
            mentee.add(m);
        }
        return mentee;
    }
    public String createMentee(Mentee mentee){
        MenteeEntity entity = new MenteeEntity();
        BeanUtils.copyProperties(mentee,entity);
        menteeRepository.save(entity);
        return "Saved Successfully!!";
    }
    public boolean deleteMentee(Long id){
        if(menteeRepository.existsById(id)) {
            menteeRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
