package com.spring.demo.controllers;

import com.spring.demo.db.UserRepository;
import com.spring.demo.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RestController
@RequestMapping("api/profile")
public class ProfileController {
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    private UserRepository repository;

    @PutMapping("/username")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity<Object> updateUsername(@RequestBody User username, Principal principal){
        User user = repository.findDistinctFirstByUsernameIgnoreCase(principal.getName());
        User newUsername = repository.findDistinctFirstByUsernameIgnoreCase(username.getUsername());

        if (user != null) {
            if (newUsername == null) {
                user.setUsername(username.getUsername());
                repository.save(user);
                return ResponseEntity.status(HttpStatus.OK).build();
            }else {
                throw new ResponseStatusException(HttpStatus.NO_CONTENT, "The name is already taken");
            }
        }
        throw new ResponseStatusException(HttpStatus.NO_CONTENT, "User does not exist");
    }

    @PutMapping("/password")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public String updateUserPassword(@RequestBody User password, Principal principal){
        User user = repository.findDistinctFirstByUsernameIgnoreCase(principal.getName());
        String newPassword = encoder.encode(password.getPassword());
        System.out.println(newPassword);
        user.setPassword(newPassword);
        repository.save(user);
        return "Your username has updated";
    }


}
