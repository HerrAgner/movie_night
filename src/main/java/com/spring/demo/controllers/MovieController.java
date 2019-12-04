package com.spring.demo.controllers;

import com.spring.demo.entities.Movie;
import com.spring.demo.services.MovieService;
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

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public ResponseEntity<Movie> getOneMovie(@RequestParam(required = false) String t,
                                             @RequestParam(required = false) String i) {
//        Movie movie = omdbService.findOneMovie(t, i);
        Movie movie = null;
        if (i != null)
            movie = movieService.getMovieById(i);

        if (movie == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(movie, HttpStatus.OK);
    }

}
