package com.spring.demo.controllers;

import com.spring.demo.entities.Movie;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("")
public class DescriptionController {

    @GetMapping
    String getAllMovies(){
        return "<h3>Welcome</h3>" +
                "<p>   To get All the movies add <em>\"/api/movies\"</em> to the url.</p>" +
                "<p>   To get one specific movie add <em>\"/api/movies/MOVIE_ID\"</em> to the url.</p>" +
                "<p>   To get one movie by title add <em>\"/api/movies/search/title/?title=MOVIE_NAME\"</em> to the url.</p>" +
                "<p>   To get movies by rating add <em>\"/api/movies/search/rating/?imdbRating=8.5\"</em> to the url.</>" +
                "<p>   To create a movie add <em>\"/api/movies/newMovie\"</em> to the url + " +
                " RequestBody (String title, float imdbRating) (Postman).</>" +
                "<p>   To delete a movie add <em>\"/api/movies/MOVIE_ID\"</em> to the url (Postman).</>" +
                "<p>   To update a movie add <em>\"/api/movies/MOVIE_ID\"</em> to the url (Postman) +" +
                " RequestBody (Postman)";
    }
}
