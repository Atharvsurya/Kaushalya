package com.kaushalya.web.Service;

import com.kaushalya.web.Entity.UserEntity;
import com.kaushalya.web.Repository.UserRepository;
import com.kaushalya.web.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    public User findByEmail(String email) {
        UserEntity entity = userRepository.findByEmail(email);

        if(entity==null) return null;

        User user = new User();
        BeanUtils.copyProperties(entity, user);
        return user;
    }

    public boolean createUser(User user) {
        UserEntity entity = new UserEntity();
        BeanUtils.copyProperties(user, entity, "pass");
        String securePass = encoder.encode(user.getPass());
        entity.setPass(securePass);
        userRepository.save(entity);
        return true;
    }
}
