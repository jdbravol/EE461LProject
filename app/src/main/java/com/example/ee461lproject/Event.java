package com.example.ee461lproject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

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
    private HashMap<String, Boolean> rsvpList;

    public ArrayList<String> getRSVPList(){
        ArrayList<String> rsvp = new ArrayList<String>();

        for(String s : rsvpList.keySet()){
            if(rsvpList.get(s) == true){
                rsvp.add(s);
            }
        }

        return rsvp;
    }

    public boolean isInRSVPList(String user){
        if(rsvpList.containsKey(user)){
            return true;
        }

        else{
            return false;
        }
    }

    public Event(String eventName, String organizer, Date date, String location, String description) {
        this.eventName = eventName;
        this.organizer = organizer;
        this.date = date;
        this.location = location;
        this.description = description;
        rsvpList = new HashMap<String, Boolean>();
    }

    public Event(){

    }
    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public void addToRSVP(String user){
        rsvpList.put(user, true);
    }

    public void removeOfRSVP(String user){
        rsvpList.remove(user);
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
