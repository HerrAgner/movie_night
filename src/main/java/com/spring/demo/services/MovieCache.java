package com.spring.demo.services;

import com.spring.demo.db.MovieRepository;
import com.spring.demo.db.SearchResultRepository;
import com.spring.demo.entities.Movie;
import com.spring.demo.entities.SearchResult;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MovieCache {

    private static Set<String> movieCache;
    private static Set<SearchResult> movieSearchCache;
    private static MovieRepository movieRepository;
    private static SearchResultRepository searchResultRepository;

    public MovieCache(MovieRepository movieRepository, SearchResultRepository searchResultRepository) {
        this.movieRepository = movieRepository;
        MovieCache.searchResultRepository = searchResultRepository;
        movieCache = movieRepository.findAll().stream().parallel().map(Movie::getImdbID).collect(Collectors.toSet());
        movieSearchCache = searchResultRepository.findAll().stream().parallel().collect(Collectors.toSet());
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

    public static void addSearchToCache(SearchResult searchResult) {
        if (searchResult != null && searchResult.getTotalResults() > 0 && !movieSearchCache.contains(searchResult)) {
            movieSearchCache.add(searchResult);
            searchResultRepository.insert(searchResult);
        }
    }

    public static SearchResult getSearchFromCache(String searchText, int page) {
        return searchResultRepository.findFirstBySearchTextAndPage(searchText, page);
    }
}
