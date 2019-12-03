package com.spring.demo.services;

import com.spring.demo.entities.Movie;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OmdbService {
    private String omdbUrl = "http://www.omdbapi.com/";
    private String apikey = "&apikey=4441eb6e";
    private RestTemplate omdbRestTemplate = new RestTemplate();


    public Movie findOneMovie(String t, String i) {
        Movie movie = null;
        if (t != null && !t.isEmpty()) {
            movie = findOneMovieByTitle(t);
        } else if (i != null && !i.isEmpty()) {
            movie = findOneMovieById(i);
        }
        if (movie.getTitle() == null) {
            return null;
        }
        return movie;
    }

    public Movie findOneMovieByTitle(String title) {
        return omdbRestTemplate.getForObject(omdbUrl + "?t=" + title + apikey, Movie.class);

    }

    public Movie findOneMovieById(String id) {
        return omdbRestTemplate.getForObject(omdbUrl + "?i=" + id + apikey, Movie.class);
    }


}
