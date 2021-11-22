package com.example.laikipiauniversityapp.models;

public class ModelEventList {

    public String eventId;
    String eventTitle;
    String eventDescription;
    String eventIcon;
    String timestamp;
    String eventDate;
    String eventHost;
    String eventVenue;
    String CreatedBy;

    public ModelEventList(String eventId,String eventDate, String eventTitle, String eventDescription, String eventIcon, String timestamp, String eventHost,String eventvenue,String createdBy) {
        this.eventId = eventId;
        this.eventDate =eventDate;
        this.eventTitle = eventTitle;
        this.eventDescription = eventDescription;
        this.eventIcon = eventIcon;
        this.timestamp = timestamp;
        this.eventHost=eventHost;
        this.eventVenue=eventvenue;
        CreatedBy = createdBy;
    }

    public String getEventVenue() {
        return eventVenue;
    }

    public void setEventVenue(String eventVenue) {
        this.eventVenue = eventVenue;
    }

    public String getEventHost() {
        return eventHost;
    }

    public void setEventHost(String eventHost) {
        this.eventHost = eventHost;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventId() {
        return eventId;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public String getEventIcon() {
        return eventIcon;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public ModelEventList() {

    }
}
