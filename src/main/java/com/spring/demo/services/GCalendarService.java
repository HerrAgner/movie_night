package com.spring.demo.services;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GCalendarService {


    private final GAuthService gAuthService;
    private final RestTemplate restTemplate = new RestTemplate();
    private final String FREE_BUSY_URL = "https://www.googleapis.com/calendar/v3/freeBusy";
    private final long FREE_BUSY_NUM_DAYS_AHEAD = 30;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public GCalendarService(GAuthService gAuthService) {
        this.gAuthService = gAuthService;
    }

    public FreeBusyResponse getFreeBusy(String username) {
        //Get access_token from db
        String bearerToken = gAuthService.getBearerTokenForUser(username);

        //Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(bearerToken);

        //Create request params
        var timeMin = new DateTime(Instant.now().toString());
        var timeMax = new DateTime(Instant.now().plus(Duration.ofDays(30)).toString());
        var items = List.of(new FreeBusyRequestItem().setId("primary"));

        //Put requestparams in request
        Map<String, Object> mapReq = new HashMap<>();
        mapReq.put("timeMin", timeMin.toStringRfc3339());
        mapReq.put("timeMax", timeMax.toStringRfc3339());
        mapReq.put("items", items);

        String objAsJson = null;
        try {
            objAsJson = objectMapper.writeValueAsString(mapReq);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }

        //Create entity from request and headers
        HttpEntity entity = new HttpEntity(objAsJson, headers);


        Map<String, Object> response = null;
        try {
            response = restTemplate.postForObject(FREE_BUSY_URL, entity, Map.class);
        } catch (HttpClientErrorException e) {
            System.out.println(e.getResponseBodyAsString());
            e.printStackTrace();
            return null;
        }

        var freeBusyResponse = new FreeBusyResponse();
        try {
            freeBusyResponse.setTimeMin(new DateTime((String) response.get("timeMin")));
            freeBusyResponse.setTimeMax(new DateTime((String) response.get("timeMax")));
            freeBusyResponse.setCalendars((Map<String, FreeBusyCalendar>) response.get("calendars"));
        } catch (Exception e) {
            System.out.println("Key not present");
//            e.printStackTrace();
        }
        return freeBusyResponse;
    }
}
