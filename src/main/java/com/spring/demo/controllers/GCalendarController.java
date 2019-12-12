package com.spring.demo.controllers;


import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.TimePeriod;
import com.spring.demo.entities.MovieEvent;
import com.spring.demo.services.GCalendarService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("api/gcal")
public class GCalendarController {

    private final GCalendarService gCalendarService;

    public GCalendarController(GCalendarService gCalendarService) {
        this.gCalendarService = gCalendarService;
    }

    @PostMapping
    public ResponseEntity<List<TimePeriod>> getBusyAndFreePeriods(@RequestParam(defaultValue = "0") long duration, @RequestBody List<String> requestForUsers) {
        try {
            var suggestedEventPeriods = gCalendarService.getSuggestedEventPeriods(requestForUsers, duration);
            return new ResponseEntity<>(suggestedEventPeriods, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("events/create")
    public ResponseEntity<Event> createEvent(@RequestBody MovieEvent event){
        Event newEvent = gCalendarService.createCalendarEvent(event);
        if (newEvent == null || newEvent.getCreator() == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(newEvent, HttpStatus.OK);
    }
}
