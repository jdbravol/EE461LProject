package com.example.ee461lproject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ezuec on 4/22/2017.
 */

public class Event {

    private String uniqueID;
    private String eventName;
    private String organizer;
    private Date date;
    private String location;
    private String description;
    private ArrayList<String> rsvpList;

    public Event(String eventName, String organizer, Date date, String location, String description) {
        this.eventName = eventName;
        this.organizer = organizer;
        this.date = date;
        this.location = location;
        this.description = description;
        rsvpList = new ArrayList<String>();
    }

    public void addToRSVP(String email){
        rsvpList.add(email);
    }

    public void removeOfRSVP(String email){
        rsvpList.remove(email);
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
