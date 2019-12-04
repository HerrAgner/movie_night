package com.spring.demo.services;

import com.spring.demo.entities.MovieSearchResult;
import com.spring.demo.entities.SearchResult;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieSearchService {

    private final OmdbService omdbService;

    public MovieSearchService(OmdbService omdbService) {
        this.omdbService = omdbService;
    }

    public SearchResult searchMovies(String query, int page) {
        return omdbService.searchMovies(query, page);
    }
}
