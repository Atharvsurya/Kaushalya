package com.kaushalya.backend.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class testAPI {

    @GetMapping("/test")
    public String hello() {
        return "Hello from Spring Boot backend!";
    }
}
