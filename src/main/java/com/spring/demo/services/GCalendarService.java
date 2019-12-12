package com.spring.demo.services;


import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.*;
import com.google.auth.oauth2.GoogleCredentials;
import com.spring.demo.util.SuperSecretInformation;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class GCalendarService {


    private final GAuthService gAuthService;
    private final SuperSecretInformation superSecretInformation;
    private final String FREE_BUSY_URL = "https://www.googleapis.com/calendar/v3/freeBusy";
    private final long NUM_DAYS_AHEAD = 30;
    private final long NUM_DAYS_START = 0;
    private final long PADDING_DURATION_FOR_EVENTS_IN_MIN = 30;
    private final Set<Integer> ACCEPTED_START_HOURS = Set.of(18, 19, 20, 21, 22, 23);
    private final Set<Integer> ACCEPTED_END_HOURS = Set.of(20, 21, 22, 23, 0, 1, 2);

    public GCalendarService(GAuthService gAuthService, SuperSecretInformation superSecretInformation) {
        this.gAuthService = gAuthService;
        this.superSecretInformation = superSecretInformation;
    }

    public List<TimePeriod> getSuggestedEventPeriods(List<String> users, long duration) {
        var freeBusyResponses = getAllFreeBusyResponse(users);
        var busyPeriods = getBusyPeriods(freeBusyResponses);
        var mergedBusyPeriods = mergeBusyPeriods(busyPeriods);
        var invertBusyPeriods = invertBusyPeriods(mergedBusyPeriods, duration);
        return suggestEventPeriods(invertBusyPeriods, duration);
    }

    private FreeBusyRequest getFreeBusyRequest(long start, long end) {
        var request = new FreeBusyRequest();
        var timeMin = new DateTime(Instant.now().plus(Duration.ofDays(start)).toString());
        var timeMax = new DateTime(Instant.now().plus(Duration.ofDays(end)).toString());
        request.setTimeMin(timeMin);
        request.setTimeMax(timeMax);
        request.setItems(List.of(new FreeBusyRequestItem().setId("primary")));
        return request;
    }

    public FreeBusyResponse getFreeBusyFromCalendar(Calendar calendar) {

        if (calendar == null) return null;

        var request = getFreeBusyRequest(NUM_DAYS_START, NUM_DAYS_AHEAD);
        FreeBusyResponse response;
        try {
            response = calendar.freebusy().query(request).execute();
        } catch (IOException io) {
            System.out.println("Connection timed out.....");
            return null;
        }
        return response;
    }

    public Calendar getCalendar(String username) {
        var accessToken = gAuthService.getAccessToken(username);
        if (accessToken == null) return null;
        gAuthService.tryRefreshToken(username);
        GoogleCredentials credential = GoogleCredentials.create(accessToken);
        if (credential == null || credential.getAccessToken() == null) return null;
        return new Calendar.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(), new GoogleCredential().setAccessToken(credential.getAccessToken().getTokenValue()))
                .setApplicationName(superSecretInformation.getApplicationName())
                .build();
    }

    public List<TimePeriod> getBusyPeriods(List<FreeBusyResponse> freeBusyResponses) {
        return freeBusyResponses.size() > 0 ?
                freeBusyResponses.parallelStream()
                        .map(FreeBusyResponse::getCalendars)
                        .map(Map::values)
                        .flatMap(fbCal -> fbCal
                                .stream()
                                .map(FreeBusyCalendar::getBusy))
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList()) : null;
    }

    public List<TimePeriod> mergeBusyPeriods(List<TimePeriod> arr) {
        if (arr.size() <= 0)
            return null;
        Stack<TimePeriod> stack = new Stack<>();
        arr.sort((i1, i2) -> (int) (i1.getStart().getValue() - i2.getStart().getValue()));
        stack.push(arr.get(0));
        for (int i = 1; i < arr.size(); i++) {
            TimePeriod top = stack.peek();
            if (top.getEnd().getValue() < arr.get(i).getStart().getValue())
                stack.push(arr.get(i));
            else if (top.getEnd().getValue() < arr.get(i).getEnd().getValue()) {
                top.setEnd(arr.get(i).getEnd());
                stack.pop();
                stack.push(top);
            }
        }
        return new ArrayList<>(stack);
    }

    public List<TimePeriod> suggestEventPeriods(List<TimePeriod> freePeriods, long durationAsMin) {
        List<TimePeriod> availablePeriods = new ArrayList<>();

        ZonedDateTime zon = ZonedDateTime.ofInstant(Instant.now(), ZoneOffset.UTC).plus(Duration.ofHours(1)).truncatedTo(ChronoUnit.SECONDS);
        zon = zon.minus(Duration.ofMinutes(zon.getMinute()));
        zon = zon.minus(Duration.ofSeconds(zon.getSecond()));
        while (zon.isBefore(ZonedDateTime.ofInstant(Instant.now().plus(Duration.ofDays(30)), ZoneOffset.UTC))) {
            var eventDuration = Duration.ofMinutes(durationAsMin + (PADDING_DURATION_FOR_EVENTS_IN_MIN * 2));
            if (ACCEPTED_START_HOURS.contains(zon.getHour()) && ACCEPTED_END_HOURS.contains(zon.plus(eventDuration).getHour())) {
                for (var free : freePeriods) {
                    if (zon.toEpochSecond() * 1000 >= free.getStart().getValue() && zon.plus(eventDuration).toEpochSecond() * 1000 < free.getEnd().getValue()) {
                        var freePeriod = new TimePeriod();
                        freePeriod.setStart(new DateTime(zon.toInstant().toString()));
                        freePeriod.setEnd(new DateTime(zon.plus(eventDuration).toInstant().toString()));
                        availablePeriods.add(freePeriod);
                    }
                }
            }
            zon = zon.plusHours(1);
        }
        availablePeriods.forEach(p -> System.out.println(p.getStart().toStringRfc3339() + " - " + p.getEnd().toStringRfc3339()));
        return availablePeriods;
    }

    public ArrayList<TimePeriod> invertBusyPeriods(List<TimePeriod> busyPeriods, long durationAsMin) {
        var durationAsMilli = TimeUnit.MILLISECONDS.convert(Duration.ofMinutes(durationAsMin));
        var freePeriods = new ArrayList<TimePeriod>();

//        if (!busyPeriods.isEmpty() && busyPeriods.get(0).getStart().getValue() < Instant.now().toEpochMilli() && busyPeriods.get(0).getEnd().getValue() > Instant.now().toEpochMilli()) {
//        } else

        if (!busyPeriods.isEmpty() && busyPeriods.get(0).getStart().getValue() > Instant.now().toEpochMilli()) {
            var startOfFreePeriod = new DateTime(Date.from(Instant.now()));
            var endOfFreePeriod = new DateTime(Date.from(Instant.ofEpochMilli(busyPeriods.get(0).getStart().getValue())));
            var freePeriod = new TimePeriod();
            freePeriod.setStart(startOfFreePeriod);
            freePeriod.setEnd(endOfFreePeriod);
            freePeriods.add(freePeriod);
        }

        var i = 0;
        for (var period : busyPeriods) {
            TimePeriod nextPeriod = null;
            try {
                nextPeriod = busyPeriods.get(i + 1);
            } catch (Exception e) {
            }

            var startOfFreePeriod = period.getEnd().getValue() < Instant.now().toEpochMilli() ? new DateTime(Date.from(Instant.now())) : new DateTime(Date.from(Instant.ofEpochMilli(period.getEnd().getValue())));
            DateTime endOfFreePeriod;
            if (nextPeriod != null && (nextPeriod.getStart().getValue() - period.getEnd().getValue()) > durationAsMilli) {
                endOfFreePeriod = new DateTime(Date.from(Instant.ofEpochMilli(nextPeriod.getStart().getValue())));
            } else {
                endOfFreePeriod = new DateTime(Date.from(Instant.ofEpochMilli(period.getEnd().getValue()).plus(Duration.ofDays(30))));
            }
            var freePeriod = new TimePeriod();
            freePeriod.setStart(startOfFreePeriod);
            freePeriod.setEnd(endOfFreePeriod);
            freePeriods.add(freePeriod);
            i++;
        }
        return freePeriods;
    }

    public List<FreeBusyResponse> getAllFreeBusyResponse(List<String> users) {
        var freeBusyList = new ArrayList<FreeBusyResponse>();
        for (String username : users) {
            var calendar = getCalendar(username);
            if (calendar != null) {
                var freeBusyResponse = getFreeBusyFromCalendar(calendar);
                freeBusyList.add(freeBusyResponse);
            }
        }
        return freeBusyList;
    }
}

