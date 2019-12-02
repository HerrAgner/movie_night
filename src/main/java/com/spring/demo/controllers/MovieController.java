package com.spring.demo.controllers;

import com.spring.demo.db.MovieRepository;
import com.spring.demo.entities.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("api/movies")
public class MovieController {

    @Autowired
    private MovieRepository movieCollection;

    @GetMapping("{id}")
    ResponseEntity<Optional<Movie>> getOneMovie(@PathVariable String id){
        if (movieCollection.findById(id).isPresent()) {
            return ResponseEntity.ok(movieCollection.findById(id));
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    //t.ex. http://localhost:8080/api/movies/search/title/?title=papi
    @GetMapping("/search/title")
    ResponseEntity<Object> getOneMovieByTitle(@RequestParam String title){
        if (!movieCollection.findByTitleContaining(title).isEmpty()) {
            return ResponseEntity.ok(movieCollection.findByTitleContaining(title));
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    //t.ex. http://localhost:8080/api/movies/search/rating/?imdbRating=8.5
    @GetMapping("/search/rating")
    List<Movie> getOneMovieByImdbRating(@RequestParam float imdbRating){
        return movieCollection.findByImdbRating(imdbRating);
    }

    @GetMapping
    ResponseEntity<Object> getAllMovies(){
        List<Movie> movies = movieCollection.findAll();
        if (!movies.isEmpty()) {
            return ResponseEntity.ok(movies);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/newMovie")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public String createMove(@RequestBody Movie movie){
        if (movie.getTitle() != null && movie.getImdbRating() != 0.0 && movie.getId() == null) {
            if(movieCollection.findByTitle(movie.getTitle()) == null) {
                movieCollection.save(movie);
                return "New item added";
            }
            return "The title of the movie is already taken";
        }
        return "Invalid object";
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @DeleteMapping("{id}")
    public ResponseEntity deleteMovie(@PathVariable String id){
        if (movieCollection.findById(id).isPresent()) {
            movieCollection.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        throw new ResponseStatusException(HttpStatus.NO_CONTENT, "NO SUCH MOVIE");
    }

    @PutMapping("{id}")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public String updateMovie(@RequestBody Movie movie, @PathVariable String id){
        Optional<Movie> theMovie = movieCollection.findById(id);
        if (!theMovie.isPresent()){
            return "Movie with the id " + id + " not found";
        }

        if (movie.getImdbRating() != 0.0){
            theMovie.get().setImdbRating(movie.getImdbRating());
        }

        if (movie.getTitle() != null){
            theMovie.get().setTitle(movie.getTitle());
        }

        movieCollection.save(theMovie.get());

        return "Movie with the id " + id + " updated";
    }
}
