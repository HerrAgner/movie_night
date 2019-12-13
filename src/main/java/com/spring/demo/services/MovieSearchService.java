package com.spring.demo.services;

import com.spring.demo.entities.SearchResult;
import org.springframework.stereotype.Service;

@Service
public class MovieSearchService {

    private final OmdbService omdbService;


    public MovieSearchService(OmdbService omdbService) {
        this.omdbService = omdbService;
    }

    public SearchResult searchMovies(String query, int page) {

        SearchResult cachedSearch = MovieCache.getSearchFromCache(query);

        if (cachedSearch != null && cachedSearch.getTotalResults() > 0) {
            cachedSearch.setSearchText(query);
            return cachedSearch;
        } else {
            SearchResult search = omdbService.searchMovies(query, page);
            search.setSearchText(query);
            MovieCache.addSearchToCache(search);
            return search;
        }
    }
}
