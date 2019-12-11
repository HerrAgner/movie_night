package com.spring.demo.services;

import com.spring.demo.db.MovieEventRepository;
import com.spring.demo.entities.MovieEvent;
import org.springframework.stereotype.Service;

@Service
public class MovieEventService {

    private final MovieEventRepository movieEventRepository;

    public MovieEventService(MovieEventRepository movieEventRepository){
        this.movieEventRepository = movieEventRepository;
    }

    public void saveMovieEventToDb(MovieEvent event){
        movieEventRepository.save(event);
    }

    public MovieEvent getMovieEvent(MovieEvent event){
        return movieEventRepository.findByEventId(event.getEventId());
    }
}
