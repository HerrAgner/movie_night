package com.spring.demo.controllers;

import com.spring.demo.db.UserRepository;
import com.spring.demo.entities.User;
import com.spring.demo.services.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/event")
public class EventController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public Object getUsers(){
        List<User> users =userRepository.findAll();
        return users;
    }
}
