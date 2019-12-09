package com.spring.demo.services;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.*;
import com.google.auth.oauth2.GoogleCredentials;
import com.spring.demo.util.SuperSecretInformation;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

@Service
public class GCalendarService {


    private final GAuthService gAuthService;
    private final SuperSecretInformation superSecretInformation;
    private final String FREE_BUSY_URL = "https://www.googleapis.com/calendar/v3/freeBusy";
    private final long NUM_DAYS_AHEAD = 30;
    private final long NUM_DAYS_START = 0;

    public GCalendarService(GAuthService gAuthService, SuperSecretInformation superSecretInformation) {
        this.gAuthService = gAuthService;
        this.superSecretInformation = superSecretInformation;
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
        FreeBusyResponse response = null;
        try {
            response = calendar.freebusy().query(request).execute();
        } catch (IOException io) {
            io.printStackTrace();
        }
        return response;
    }

    public List<Event> getEventsFromCalendar(Calendar calendar) {
        Events events = null;
        var now = new DateTime(Instant.now().toString());
        try {
            events = calendar.events().list("primary")
                    .setMaxResults(10)
                    .setTimeMin(now)
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return events.getItems();
    }

    public Calendar getCalendar(String username) {
        gAuthService.tryRefreshToken(username);
        GoogleCredentials credential = GoogleCredentials.create(gAuthService.getAccessToken(username));
        if (credential == null || credential.getAccessToken() == null) return null;
        return new Calendar.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(), new GoogleCredential().setAccessToken(credential.getAccessToken().getTokenValue()))
                .setApplicationName(superSecretInformation.getApplicationName())
                .build();
    }

    public Event createCalendarEvent(String creator, String startTime, String endTime, String timeZone, List<String> attendees) {
        Calendar calendar = getCalendar(creator);
        TimeZone tz = TimeZone.getTimeZone(timeZone);
        String offset = tz.toZoneId().getRules().getStandardOffset(Instant.now()).getId();
        Event event = new Event()
                .setSummary("Movie Night");
        DateTime startDateTime = new DateTime(startTime + offset);
//        2015-05-28T09:00:00-07:00
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime);
        event.setStart(start);

        DateTime endDateTime = new DateTime(endTime + offset);
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime);
        event.setEnd(end);


        ArrayList<EventAttendee> eventAttendees = new ArrayList<>();
        for (String attendee : attendees) {
            eventAttendees.add(new EventAttendee().setEmail(attendee));
        }
//        EventAttendee[] eventAttende = new EventAttendee[]{
//                new EventAttendee().setEmail("test@asd.se"),
//                new EventAttendee().setEmail("sbrin@example.com"),
//        };
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
            System.out.printf("Event created: %s\n", event.getHtmlLink());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return event;
    }
}

