package com.spring.demo.db;

import com.spring.demo.entities.MovieEvent;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface MovieEventRepository extends MongoRepository<MovieEvent, String> {

    @Query("{'$or' : [{'attendees' : ?0}, {'creator' : ?0}]}")
    List<MovieEvent> findAllByAttendeesContainsOrCreator(String email, Pageable pageable);
    MovieEvent findByEventId(String eventId);
    void deleteByEventId(String eventId);

}
