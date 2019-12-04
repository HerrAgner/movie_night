package com.spring.demo.controllers;

import com.spring.demo.entities.Movie;
import com.spring.demo.services.OmdbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/movies")
public class MovieController {

    @Autowired
    private OmdbService omdbService;

    @GetMapping
    public ResponseEntity<Movie> getOneMovie(@RequestParam(required = false) String t,
                                             @RequestParam(required = false) String i) {
        Movie movie = omdbService.findOneMovie(t, i);
        if (movie == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(movie, HttpStatus.OK);
    }

}
