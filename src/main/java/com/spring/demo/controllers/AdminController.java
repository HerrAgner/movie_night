package com.spring.demo.controllers;

import com.spring.demo.db.UserRepository;
import com.spring.demo.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("api/admin")
public class AdminController {

    @Autowired
    private UserRepository repository;

    @GetMapping("/users")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<List<User>> getUsers(){
        List<User> users = repository.findAll();
        if (!users.isEmpty()) {
            return ResponseEntity.ok(users);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("users/{id}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity deleteUser(@PathVariable String id){
        if (repository.findById(id).isPresent()) {
            repository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        throw new ResponseStatusException(HttpStatus.NO_CONTENT, "NO SUCH USER");
    }
}
