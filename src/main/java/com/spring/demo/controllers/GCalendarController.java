package com.spring.demo.controllers;


import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.FreeBusyResponse;
import com.spring.demo.entities.MovieEvent;
import com.spring.demo.entities.MovieEvent;
import com.spring.demo.services.GCalendarService;
import com.spring.demo.services.MovieEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    public ResponseEntity<Map<String, FreeBusyResponse>> getFreeBusyResponse(@RequestBody List<String> requestForUsers) {
        var response = new HashMap<String, FreeBusyResponse>();
        for (String username : requestForUsers) {
            var calendar = gCalendarService.getCalendar(username);
            var freeBusyResponse = gCalendarService.getFreeBusyFromCalendar(calendar);
            response.put(username, freeBusyResponse);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

//    @PostMapping("events")
//    public ResponseEntity<Map<String, List<Event>>> getEvents(@RequestBody List<String> requestForUsers) {
//        var response = new HashMap<String, List<Event>>();
//        for (String username : requestForUsers) {
//            var calendar = gCalendarService.getCalendar(username);
//            var events = gCalendarService.getEventsFromCalendar(calendar);
//            response.put(username, events);
//        }
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }

    @PostMapping("events/create")
    public ResponseEntity<Event> createEvent(@RequestBody MovieEvent event){
        Event newEvent = gCalendarService.createCalendarEvent(event);
        if (newEvent == null || newEvent.getCreator() == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(newEvent, HttpStatus.OK);
    }

    @PutMapping("event")
    public ResponseEntity<Event> updateEvent(@RequestBody MovieEvent event){
        MovieEvent movieEvent = movieEventService.getMovieEvent(event);
        movieEvent.setEventId(event.getMovieId());
        movieEvent.setAttendees(event.getAttendees());
        movieEvent.setCreator(event.getCreator());
        movieEvent.setEndTime(event.getEndTime());
        movieEvent.setEventName(event.getEventName());
        movieEvent.setMovieId(event.getMovieId());
        movieEvent.setStartTime(event.getStartTime());
        movieEvent.setTimeZone(event.getTimeZone());
        movieEventService.saveMovieEventToDb(event);

        return new ResponseEntity<>( HttpStatus.OK);
    }

    @DeleteMapping("event/{id}")
    public ResponseEntity<Event> deleteEvent(@PathVariable String id, Principal principal){
        System.out.println(id);
        System.out.println(principal.getName());
        movieEventService.deleteMovieEvent(id);
        var calendar = gCalendarService.getCalendar(principal.getName());

        try {
            calendar.events().delete("primary", id).execute();
            System.out.println("the event deleted from google calendar!");
        } catch (IOException e) {
            System.out.println(e);
            return new ResponseEntity<>( HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>( HttpStatus.OK);
    }
}
