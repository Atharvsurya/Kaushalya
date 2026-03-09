package com.kaushalya.web.Controller;

import com.kaushalya.web.Service.UserService;
import com.kaushalya.web.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class UserController {

    @Autowired
    public UserService userService;

    @PostMapping("/newuser")
    public boolean createUser(@RequestBody User user) {
        if(userService.findByEmail(user.getEmail())==null)
            return userService.createUser(user);
        return false;
    }

    @Autowired
    private BCryptPasswordEncoder encoder;

    @PostMapping("/userlogin")
    public ResponseEntity<?> userlogin(@RequestBody User loginData) {
        User user = userService.findByEmail(loginData.getEmail());

        if (user != null && encoder.matches(loginData.getPass(), user.getPass())) {
            return ResponseEntity.ok(user);
        }

        return ResponseEntity.status(401).body("{\"message\": \"Invalid Email or Password\"}");
    }
}