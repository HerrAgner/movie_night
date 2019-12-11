package com.spring.demo.db;

import com.spring.demo.entities.MovieEvent;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MovieEventRepository extends MongoRepository<MovieEvent, String> {

    List<MovieEvent> findAllByAttendeesContains(String email);
    List<MovieEvent> findAllByCreator(String email);
    MovieEvent findByEventId(String eventId);

}
