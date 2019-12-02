package com.spring.demo.db;

import com.spring.demo.entities.Movie;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MovieRepository extends MongoRepository <Movie, String> {

    Movie findByTitle(String title);
    List<Movie> findByTitleContaining(String title);
    List<Movie> findByImdbRating(float rating);

}
