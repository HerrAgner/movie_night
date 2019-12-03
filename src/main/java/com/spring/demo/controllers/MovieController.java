package com.spring.demo.controllers;


import com.spring.demo.entities.Movie;
import com.spring.demo.services.MovieCache;
import com.spring.demo.services.MovieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("api/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public ResponseEntity<Movie> getMovie(@RequestParam(defaultValue = "") String i) {
        System.out.println(MovieCache.getMovieCache().size());
        var movie = movieService.getMovieById(i);
        System.out.println(MovieCache.getMovieCache().size());
        return new ResponseEntity<>(movie, HttpStatus.OK);
    }
}
