package com.spring.demo.entities;

import java.util.List;

public class MovieEvent {

    private String movieId;
    private String eventName;
    private String creator;
    private String startTime;
    private String endTime;
    private String timeZone;
    private List<String> attendees;

    public MovieEvent(){
    }

    public MovieEvent(String movieId, String eventName, String creator, String startTime, String endTime, String timeZone, List<String> attendees) {
        this.movieId = movieId;
        this.eventName = eventName;
        this.creator = creator;
        this.startTime = startTime;
        this.endTime = endTime;
        this.timeZone = timeZone;
        this.attendees = attendees;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public List<String> getAttendees() {
        return attendees;
    }

    public void setAttendees(List<String> attendees) {
        this.attendees = attendees;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }
}
