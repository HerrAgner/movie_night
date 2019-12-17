package com.spring.demo.db;

import com.spring.demo.entities.Movie;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MovieRepository extends MongoRepository <Movie, String> {

}
