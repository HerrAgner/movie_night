package com.spring.demo.controllers;

import com.spring.demo.db.MovieRepository;
import com.spring.demo.db.UserRepository;
import com.spring.demo.entities.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("api/movies")
public class MovieController {
    private RestTemplate omdbRestTemplate = new RestTemplate();
    private String omdbUrl = "http://www.omdbapi.com/";
    private String databaseUrl = "http://10.152.190.96:8080/api/movies/";
    private String apikey = "&apikey=4441eb6e";

    @Autowired
    private MovieRepository movieRepository;

    @GetMapping("{title}")
    public ResponseEntity<Movie> getMovieByTitle(@PathVariable String title, HttpServletResponse response) {
        Movie movie = omdbRestTemplate.getForObject(omdbUrl + "?t=" + title + apikey, Movie.class);
        return new ResponseEntity<>(movie, HttpStatus.OK);
    }

}
