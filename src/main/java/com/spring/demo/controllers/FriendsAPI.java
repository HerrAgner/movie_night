package com.spring.demo.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/friend")
public class FriendsAPI {
    private String url = "http://10.152.190.108:8080/api/movies/";

    @GetMapping
    Object getAllMovie() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, Object.class);
    }

    @GetMapping("{id}")
    Object getOneMovie(@PathVariable String id) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url + id, Object.class);
    }

    @DeleteMapping("{id}")
    Object deleteOneMovie(@PathVariable String id) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.delete(url + id, Object.class);
        return "The movie deleted";
    }

    @PostMapping
    Object createMovie(@RequestBody Object movie) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForEntity(url, movie, Object.class);
    }
}
