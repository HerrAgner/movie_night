package com.spring.demo.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.FreeBusyRequest;
import com.google.api.services.calendar.model.FreeBusyResponse;
import com.spring.demo.services.GCalendarService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/gcal")
public class GCalendarController {

    private final GCalendarService gCalendarService;

    public GCalendarController(GCalendarService gCalendarService) {
        this.gCalendarService = gCalendarService;
    }

    @PostMapping
    public ResponseEntity<Map<String, FreeBusyResponse>> getFreeBusy(@RequestBody List<String> requestForUsers) {


        var response = new HashMap<String, FreeBusyResponse>();
        for (String username : requestForUsers) {
            var freeBusyResponse = gCalendarService.getFreeBusy(username);
            response.put(username, freeBusyResponse);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity printEvents() {
        gCalendarService.printEvents(SecurityContextHolder.getContext().getAuthentication().getName());
        return new ResponseEntity(HttpStatus.OK);
    }
}
