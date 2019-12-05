package com.spring.demo.services;

import com.spring.demo.db.MovieRepository;
import com.spring.demo.entities.Movie;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MovieCache {

    private static Set<String> movieCache;
    private static MovieRepository movieRepository;

    public MovieCache(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
        movieCache = movieRepository.findAll().stream().parallel().map(m -> m.getImdbID()).collect(Collectors.toSet());
    }

    public static Set<String> getMovieCache() {
        return movieCache;
    }

    public void setMovieCache(Set<String> movieCache) {
        this.movieCache = movieCache;
    }

    public static void addMovieToCache(Movie movie) {
        if (movie != null && movie.getTitle() != null) {
            movieCache.add(movie.getImdbID());
            movieRepository.insert(movie);
        }
    }

    public static Optional<Movie> getMovieFromCache(String movieId) {
        if (movieCache.contains(movieId)) {
            return movieRepository.findById(movieId);
        } else return Optional.empty();
    }
}
