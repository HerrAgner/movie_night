package com.spring.demo.controllers;

import com.spring.demo.db.MovieEventRepository;
import com.spring.demo.db.UserRepository;
import com.spring.demo.entities.MovieEvent;
import com.spring.demo.entities.User;
import com.spring.demo.services.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/event")
public class EventController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieEventRepository movieEventRepository;

    @GetMapping
    public Object getUsers(){
        List<User> users =userRepository.findAll();
        return users;
    }

    @GetMapping("/all")
    public ResponseEntity<List<MovieEvent>> getEvents(@RequestParam String username){
        List<MovieEvent> events = movieEventRepository.findAllByAttendeesContains(username);
        List<MovieEvent> eventsByCreator = movieEventRepository.findAllByCreator(username);
        events.addAll(eventsByCreator);
        return new ResponseEntity<>(events, HttpStatus.OK);

    }


}
