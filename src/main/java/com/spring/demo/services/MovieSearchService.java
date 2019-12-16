package com.spring.demo.services;

import com.spring.demo.entities.Movie;
import com.spring.demo.entities.SearchResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MovieSearchService {

    private final OmdbService omdbService;
    private RestTemplate suggestRestTemplate = new RestTemplate();
    private String suggestUrl = "https://v2.sg.media-imdb.com/suggests/";


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

    public String suggestMovies(String letter, String word) {
        String json = "";
        if (word.length() > 0) {
            String s = suggestRestTemplate.getForObject(suggestUrl + letter + "/" + word + ".json", String.class);
            if (s != null) {
                json = s.substring(s.indexOf("(") + 1, s.lastIndexOf(")"));
            }
        }
        return json;

    }
}
