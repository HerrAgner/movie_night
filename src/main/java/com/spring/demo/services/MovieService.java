package com.spring.demo.services;


import com.spring.demo.db.MovieRepository;
import com.spring.demo.entities.Movie;
import org.springframework.stereotype.Service;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final OmdbService omdbService;

    public MovieService(MovieRepository movieRepository, OmdbService omdbService) {
        this.movieRepository = movieRepository;
        this.omdbService = omdbService;
    }

    public Movie getMovieById(String id) {

        var cachedMovie = MovieCache.getMovieFromCache(id);
        if (cachedMovie.isPresent()) {
            return cachedMovie.get();
        } else {
            var movie = omdbService.findOneMovieById(id);
            MovieCache.addMovieToCache(movie);
            return movie;
        }
    }
}
