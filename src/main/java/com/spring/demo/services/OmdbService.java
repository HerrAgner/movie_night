package com.spring.demo.services;

import com.spring.demo.entities.Movie;
import com.spring.demo.entities.SearchResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OmdbService {
    private String omdbUrl = "http://www.omdbapi.com/";
    private String apikey = "&apikey=4441eb6e";
    private RestTemplate omdbRestTemplate = new RestTemplate();
    private String type = "movie";


    public Movie findOneMovie(String t, String i) {
        Movie movie = null;
        if (t != null && !t.isEmpty()) {
            movie = findOneMovieByTitle(t);
        } else if (i != null && !i.isEmpty()) {
            movie = findOneMovieById(i);
        }
        if ((movie != null ? movie.getTitle() : null) == null) {
            return null;
        }
        return movie;
    }

    public Movie findOneMovieByTitle(String title) {
        return omdbRestTemplate.getForObject(omdbUrl + "?t=" + title + apikey, Movie.class);
    }

    public Movie findOneMovieById(String id) {
        Movie movie = null;
        try {
            movie = omdbRestTemplate.getForObject(omdbUrl + "?i=" + id + apikey, Movie.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return movie != null && movie.getTitle() != null ? movie : null;
    }

    public SearchResult searchMovies(String query, int page) {
        query =  query.replace(" ", "+");
        var url = omdbUrl + "?s=" + query + "&page=" + page + apikey;
        return omdbRestTemplate.getForObject(url, SearchResult.class);
    }


}
