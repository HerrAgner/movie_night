package com.spring.demo.services;


import com.spring.demo.db.MovieRepository;
import com.spring.demo.entities.Movie;
import org.springframework.stereotype.Service;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Movie getMovieById(String id) {

        var cachedMovie = MovieCache.getMovieFromCache(id);
        if (cachedMovie.isPresent()) {
            System.out.println("Cached movie");
            return cachedMovie.get();
        } else {
            // Add request to OmdbService
            Movie movie = new Movie();
            movie.setId(id);

            MovieCache.addMovieToCache(movie);
            return movie;
        }
    }
}
