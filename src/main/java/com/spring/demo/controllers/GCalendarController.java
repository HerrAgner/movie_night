package com.spring.demo.controllers;


import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.TimePeriod;
import com.spring.demo.entities.MovieEvent;
import com.spring.demo.services.GCalendarService;
import com.spring.demo.services.MovieEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("api/gcal")
public class GCalendarController {

    private final GCalendarService gCalendarService;

    public GCalendarController(GCalendarService gCalendarService) {
        this.gCalendarService = gCalendarService;
    }

    @Autowired
    MovieEventService movieEventService;

    @PostMapping
    public ResponseEntity<List<TimePeriod>> getBusyAndFreePeriods(@RequestParam(defaultValue = "0") long duration, @RequestBody List<String> requestForUsers) {
        try {
            requestForUsers.add(SecurityContextHolder.getContext().getAuthentication().getName());
            var suggestedEventPeriods = gCalendarService.getSuggestedEventPeriods(requestForUsers, duration);
            if (suggestedEventPeriods != null) {
                return new ResponseEntity<>(suggestedEventPeriods, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("events/create")
    public ResponseEntity<Event> createEvent(@RequestBody MovieEvent event) {
        Event newEvent = gCalendarService.createCalendarEvent(event);
        if (newEvent == null || newEvent.getCreator() == null) {
            return new ResponseEntity<>(HttpStatus.REQUEST_TIMEOUT);
        }
        return new ResponseEntity<>(newEvent, HttpStatus.OK);
    }

    @PutMapping("event")
    public ResponseEntity<Event> updateEvent(@RequestBody MovieEvent event) {
        MovieEvent movieEvent = movieEventService.getMovieEvent(event);
        movieEvent.setEventId(event.getEventId());
        movieEvent.setAttendees(event.getAttendees());
        movieEvent.setCreator(event.getCreator());
        movieEvent.setEndTime(event.getEndTime());
        movieEvent.setEventName(event.getEventName());
        movieEvent.setMovieId(event.getMovieId());
        movieEvent.setStartTime(event.getStartTime());
        movieEvent.setTimeZone(event.getTimeZone());

        var eventRes = gCalendarService.updateEvent(event);
        if (eventRes != null) {
            movieEventService.saveMovieEventToDb(movieEvent);
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("event/{id}")
    public ResponseEntity<Event> deleteEvent(@PathVariable String id, Principal principal) {
        movieEventService.deleteMovieEvent(id);
        var calendar = gCalendarService.getCalendar(principal.getName());

        try {
            calendar.events().delete("primary", id).execute();
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
