package com.spring.demo.services;


import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.*;
import com.google.auth.oauth2.GoogleCredentials;
import com.spring.demo.entities.MovieEvent;
import com.spring.demo.entities.User;
import com.spring.demo.util.SuperSecretInformation;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GCalendarService {


    private final GAuthService gAuthService;
    private final SuperSecretInformation superSecretInformation;
    private final MovieEventService movieEventService;
    private final UserService userService;
    private final long NUM_DAYS_AHEAD = 30;
    private final long NUM_DAYS_START = 0;
    private final long PADDING_DURATION_FOR_EVENTS_IN_MIN = 30;
    private final Set<Integer> ACCEPTED_START_HOURS = Set.of(18, 19, 20, 21);
    private final Set<Integer> ACCEPTED_END_HOURS = Set.of(20, 21, 22, 23, 0, 1);

    public GCalendarService(GAuthService gAuthService, SuperSecretInformation superSecretInformation, MovieEventService movieEventService, UserService userService) {
        this.gAuthService = gAuthService;
        this.superSecretInformation = superSecretInformation;
        this.movieEventService = movieEventService;
        this.userService = userService;
    }

    public List<TimePeriod> getSuggestedEventPeriods(List<String> users, long duration) {
        var freeBusyResponses = getAllFreeBusyResponse(users);
        var busyPeriods = getBusyPeriods(freeBusyResponses);
        var mergedBusyPeriods = mergeBusyPeriods(busyPeriods);
        var invertBusyPeriods = invertBusyPeriods(mergedBusyPeriods);

        return suggestEventPeriods(invertBusyPeriods, duration);
    }

    private FreeBusyRequest getFreeBusyRequest(long start, long end) {
        var request = new FreeBusyRequest();
        var timeMin = new DateTime(Instant.now().plus(Duration.ofDays(start)).toEpochMilli());
        var timeMax = new DateTime(Instant.now().plus(Duration.ofDays(end)).toEpochMilli());
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
            io.printStackTrace();
            return null;
        }
        return response;
    }

    public Calendar getCalendar(String username) {
        var token = gAuthService.tryRefreshToken(username);
        var accessToken = gAuthService.getAccessToken(username);
        if (accessToken == null) return null;
        GoogleCredentials credential = GoogleCredentials.create(accessToken);
        if (credential == null || credential.getAccessToken() == null) return null;
        return new Calendar.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(), new GoogleCredential().setAccessToken(accessToken.getTokenValue()))
                .setApplicationName(superSecretInformation.getApplicationName())
                .build();
    }

    public List<TimePeriod> getBusyPeriods(List<FreeBusyResponse> freeBusyResponses) {
        if (freeBusyResponses == null) return null;

        return freeBusyResponses.parallelStream()
                .map(FreeBusyResponse::getCalendars)
                .map(Map::values)
                .flatMap(fbCal -> fbCal
                        .stream()
                        .map(FreeBusyCalendar::getBusy))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public List<TimePeriod> mergeBusyPeriods(List<TimePeriod> arr) {
        if (arr == null) return null;
        if (arr.size() <= 0) return new ArrayList<>();
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
        if (freePeriods == null) return null;
        List<TimePeriod> availablePeriods = new ArrayList<>();

        ZonedDateTime zon = ZonedDateTime.ofInstant(Instant.now(), ZoneOffset.UTC).plus(Duration.ofHours(1)).truncatedTo(ChronoUnit.SECONDS);
        zon = zon.minus(Duration.ofMinutes(zon.getMinute()));
        zon = zon.minus(Duration.ofSeconds(zon.getSecond()));
        while (zon.isBefore(ZonedDateTime.ofInstant(Instant.now().plus(Duration.ofDays(30)), ZoneOffset.UTC))) {
            var eventDuration = Duration.ofMinutes(durationAsMin + (PADDING_DURATION_FOR_EVENTS_IN_MIN * 2));
            if (ACCEPTED_START_HOURS.contains(zon.getHour()) && ACCEPTED_END_HOURS.contains(zon.plus(eventDuration).getHour())) {
                for (var free : freePeriods) {
                    if (zon.toInstant().toEpochMilli() >= free.getStart().getValue() && zon.plus(eventDuration).toInstant().toEpochMilli() < free.getEnd().getValue()) {
                        var freePeriod = new TimePeriod();
                        freePeriod.setStart(new DateTime(zon.toInstant().toEpochMilli()));
                        freePeriod.setEnd(new DateTime(zon.plus(eventDuration).toInstant().toEpochMilli()));
                        availablePeriods.add(freePeriod);
                    }
                }
            }
            zon = zon.plusHours(1);
        }
        return availablePeriods;
    }

    public ArrayList<TimePeriod> invertBusyPeriods(List<TimePeriod> busyPeriods) {
        var freePeriods = new ArrayList<TimePeriod>();

        if(busyPeriods == null || busyPeriods.isEmpty()) {
            var startOfFreePeriod = new DateTime(Date.from(Instant.now()));
            var endOfFreePeriod = new DateTime(Date.from(Instant.now().plus(Duration.ofDays(30))));
            var freePeriod = new TimePeriod();
            freePeriod.setStart(startOfFreePeriod);
            freePeriod.setEnd(endOfFreePeriod);
            freePeriods.add(freePeriod);
        }

        else if (busyPeriods.get(0).getStart() != null && busyPeriods.get(0).getStart().getValue() > Instant.now().toEpochMilli()) {
            var startOfFreePeriod = new DateTime(Date.from(Instant.now()));
            var endOfFreePeriod = new DateTime(Date.from(Instant.ofEpochMilli(busyPeriods.get(0).getStart().getValue())));
            var freePeriod = new TimePeriod();
            freePeriod.setStart(startOfFreePeriod);
            freePeriod.setEnd(endOfFreePeriod);
            freePeriods.add(freePeriod);
        }

        var i = 0;
        if (busyPeriods != null) {
            for (var period : busyPeriods) {
                TimePeriod nextPeriod = null;
                try {
                    nextPeriod = busyPeriods.get(i + 1);
                } catch (Exception e) {
                }

                var startOfFreePeriod = period.getEnd().getValue() < Instant.now().toEpochMilli() ? new DateTime(Date.from(Instant.now())) : new DateTime(Date.from(Instant.ofEpochMilli(period.getEnd().getValue())));
                DateTime endOfFreePeriod;
                if (nextPeriod != null && nextPeriod.getStart().getValue() > period.getEnd().getValue()) {
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

    public Event createCalendarEvent(MovieEvent movieEvent) {
        Calendar calendar = getCalendar(movieEvent.getCreator());
        TimeZone tz = TimeZone.getTimeZone(movieEvent.getTimeZone());
        String offset = tz.toZoneId().getRules().getStandardOffset(Instant.now()).getId();
        Event event = new Event()
                .setSummary(movieEvent.getEventName())
                .setDescription("https://www.imdb.com/title/" + movieEvent.getMovieId());
        DateTime startDateTime = new DateTime(movieEvent.getStartTime() + offset);
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime);
        event.setStart(start);

        DateTime endDateTime = new DateTime(movieEvent.getEndTime() + offset);
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime);
        event.setEnd(end);


        ArrayList<EventAttendee> eventAttendees = new ArrayList<>();
        for (String attendee : movieEvent.getAttendees()) {
            User user = userService.getUserByUsername(attendee);
            gAuthService.tryRefreshToken(user.getUsername());
            if (user != null && user.getGoogleInfo() != null) {
                String email = user.getGoogleInfo().getEmail();
                if (email != null && !email.isEmpty()) {
                    eventAttendees.add(new EventAttendee().setEmail(email));
                }
            }
        }
        event.setAttendees(eventAttendees);

        EventReminder[] reminderOverrides = new EventReminder[]{
                new EventReminder().setMethod("email").setMinutes(24 * 60),
                new EventReminder().setMethod("popup").setMinutes(10),
        };
        Event.Reminders reminders = new Event.Reminders()
                .setUseDefault(false)
                .setOverrides(Arrays.asList(reminderOverrides));
        event.setReminders(reminders);

        String calendarId = "primary";
        try {
            event = calendar.events().insert(calendarId, event).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        movieEvent.setEventId(event.getId());
        movieEventService.saveMovieEventToDb(movieEvent);
        return event;
    }

    public Event updateEvent(MovieEvent event) {
        var calendar = getCalendar(event.getCreator());

        try {
            Event eventFromCalendar = calendar.events().get("primary", event.getEventId()).execute();


            ArrayList<EventAttendee> eventAttendees = new ArrayList<>();
            for (String attendee : event.getAttendees()) {
                User user = userService.getUserByUsername(attendee);
                if (user != null && user.getGoogleInfo() != null) {
                    String email = user.getGoogleInfo().getEmail();
                    if (email != null && !email.isEmpty()) {
                        eventAttendees.add(new EventAttendee().setEmail(email));
                    }
                }
            }
            eventFromCalendar.setAttendees(eventAttendees);
            eventFromCalendar.setSummary(event.getEventName());


            TimeZone tz = TimeZone.getTimeZone(event.getTimeZone());
            String offset = tz.toZoneId().getRules().getStandardOffset(Instant.now()).getId();
            DateTime startDateTime = new DateTime(event.getStartTime() + offset);
            EventDateTime start = new EventDateTime()
                    .setDateTime(startDateTime);
            eventFromCalendar.setStart(start);


            DateTime endDateTime = new DateTime(event.getEndTime() + offset);
            EventDateTime end = new EventDateTime()
                    .setDateTime(endDateTime);
            eventFromCalendar.setEnd(end);

            return calendar.events().update("primary", event.getEventId(), eventFromCalendar).execute();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

