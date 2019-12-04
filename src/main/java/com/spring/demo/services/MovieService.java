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
            return cachedMovie.get();
        } else {
            //move to OmdbService
            Movie movie = new Movie();
            movie.setId(id);


            MovieCache.addMovieToCache(movie);
            //end

            return movie;
        }
    }
}
