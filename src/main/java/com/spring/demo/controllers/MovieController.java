package com.spring.demo.controllers;

import com.spring.demo.db.MovieRepository;
import com.spring.demo.entities.Movie;
import com.spring.demo.entities.SearchResult;
import com.spring.demo.services.MovieSearchService;
import com.spring.demo.services.MovieService;
import com.spring.demo.services.OmdbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;


@RestController
@RequestMapping("api/movies")
public class MovieController {

    @Autowired
    private MovieRepository movieRepository;

    private final MovieService movieService;
    private final MovieSearchService movieSearchService;

    public MovieController(MovieService movieService, MovieSearchService movieSearchService) {
        this.movieService = movieService;
        this.movieSearchService = movieSearchService;
    }

    @GetMapping
    public ResponseEntity<Movie> getOneMovie(@RequestParam String i) {
        Movie movie = null;
        if (i != null)
            movie = movieService.getMovieById(i);

        if (movie == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(movie, HttpStatus.OK);
    }

    @GetMapping("search")
    public ResponseEntity<SearchResult> searchMovies(@RequestParam String s, @RequestParam(defaultValue = "1") int p) {
        if(p <= 0) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        SearchResult result = movieSearchService.searchMovies(s, p);

        return new ResponseEntity<>(result, result.getTotalResults() > 0 ? HttpStatus.OK : HttpStatus.NO_CONTENT);
    }


    @PostMapping()
    public ResponseEntity<ArrayList<Optional<Movie>>> getSomeMovies(@RequestBody ArrayList<Movie> moviesId) {
        ArrayList<Optional<Movie>> movies = new ArrayList<>();

        moviesId.forEach(item -> {
            movies.add(movieRepository.findById(item.getImdbID()));
            movieRepository.findById(item.getImdbID());
        });

        return  ResponseEntity.ok(movies);
    }

    @GetMapping("suggest")
    public ResponseEntity<String> suggestMovies(@RequestParam String l, @RequestParam String s) {
        String result = movieSearchService.suggestMovies(l, s);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }


}
